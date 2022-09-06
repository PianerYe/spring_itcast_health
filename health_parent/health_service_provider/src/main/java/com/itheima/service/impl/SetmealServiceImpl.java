package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 套餐服务
 * */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${out_put_path}")
    private String outPutPath;//从属性文件中读取要生成的html对应的目录

    //新增套餐信息，同时需要关联检查组
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //新增套餐，操作t_setmeal表
        setmealDao.add(setmeal);
        //设置套餐和检查组的多对多的关联关系，操作t_setmeal_checkgroup表
        Integer setmealId = setmeal.getId();
        this.setSetmealAndCheckGroup(setmealId,checkgroupIds);
        //将图片名称保存到Redis集合中
        savePic2Redis(setmeal.getImg());
        //当添加套餐后需要重新生成静态页面（套餐列表页面，套餐详情页面）
        generateMobileStaticHtml();

    }

    //生成当前方法所需的静态页面
    public void generateMobileStaticHtml(){
        //再生成静态页面之前需要查询数据
        List<Setmeal> list = setmealDao.findAll();
        //需要生成套餐列表静态页面
        generateMobileSetmealListHtml(list);
        //需要生成套餐详情静态页面
        generateMobileSetmealDetailHtml(list);
    }

    //生成套餐列表静态页面
    public void generateMobileSetmealListHtml(List<Setmeal> list){
        Map map = new HashMap<>();
        //为模板提供数据，用于生成静态页面
        map.put("setmealList",list);
        generateHtml("mobile_setmeal.ftl","m_setmeal.html",map);
    }

    //生成套餐详情静态页面(可能有多个)
    public void generateMobileSetmealDetailHtml(List<Setmeal> list){
        for (Setmeal setmeal : list) {
            Map map = new HashMap<>();
            map.put("setmeal",setmealDao.findByIdOfDetails(setmeal.getId()));
            generateHtml("mobile_setmeal_detail.ftl","setmeal_detail_"+setmeal.getId()+".html",map);
        }
    }

    //通用方法,用于生成静态页面
    public void generateHtml(String templateName,String htmlPageName,Map map){
        Configuration configuration = freeMarkerConfigurer.getConfiguration();//获得配置对象
        Writer out = null;
        try {
            Template template = configuration.getTemplate(templateName);
            //构造输出流
            out = new FileWriter(new File(outPutPath +"/" + htmlPageName));
            //输出文件
            template.process(map,out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();//查询条件
        //完成分页查询，基于mybatis框架提供的分页助手插件完成
        PageHelper.startPage(currentPage,pageSize);
        //select * from t_checkgroup limit 0,10
        Page<Setmeal> page = setmealDao.selectByCondition(queryString);
        long total = page.getTotal();
        List<Setmeal> rows = page.getResult();
        return new PageResult(total,rows);
    }

    //根据ID查询套餐
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public List<Integer> findCheckGroupIdsBySetmealId(Integer id) {
        return setmealDao.findCheckGroupIdsBySetmealId(id);
    }

    //编辑套餐
    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        //将原先图片名称从到Redis集合中删除
        Integer initSetmealId1id = setmeal.getId();
        //获取原先数据库中保存的图片数据信息
        String  initSetmealGetimg =findById(initSetmealId1id).getImg();
        deletePic2Redis(initSetmealGetimg);
        //编辑套餐，操作t_setmeal表
        setmealDao.edit(setmeal);
        Integer setmealId = setmeal.getId();
        //清除关系表
        setmealDao.deleteAssoication(setmealId);
        //设置套餐和检查组的多对多的关联关系，操作t_setmeal_checkgroup表
        this.setSetmealAndCheckGroup(setmealId,checkgroupIds);
        //将图片名称保存到Redis集合中
        savePic2Redis(setmeal.getImg());
        //当编辑套餐后需要重新生成静态页面（套餐列表页面，套餐详情页面）
        generateMobileStaticHtml();
    }

    //根据套餐ID，删除套餐
    @Override
    public void deleteById(Integer id) {
        //判断当前检查组是否已经关联到套餐
        Long count = setmealDao.findCountBySetmealId(id);
        if (count>0){
            //当前检查组已经关联到套餐，不允许删除
            throw new RuntimeException(MessageConstant.DELETE_CHECKGROUP_FAIL);
        }else {
            //获取原先数据库中保存的图片数据信息
            String  initSetmealGetimg =findById(id).getImg();
            //将原先图片名称从到Redis集合中删除
            deletePic2Redis(initSetmealGetimg);
            setmealDao.deleteById(id);
            //当删除套餐后生成的静态页面删除
                File file = new File(outPutPath+"/setmeal_detail_"+id+".html");
                if (file.exists()){
                    file.delete();
                }
            //重新生成静态页面（套餐列表页面，套餐详情页面）
            generateMobileStaticHtml();
        }

    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public Setmeal findByIdOfDetails(Integer id) {
        return setmealDao.findByIdOfDetails(id);
    }

    //建立套餐和检查组多对多关系(抽取方法)
    public void setSetmealAndCheckGroup(Integer setmealId,Integer[] checkgroupIds){
        if (checkgroupIds!=null && checkgroupIds.length > 0){
            for (Integer checkGroupId : checkgroupIds){
                Map<String,Integer> map = new HashMap<>();
                map.put("setmealId",setmealId);
                map.put("checkGroupId",checkGroupId);
                setmealDao.setSetmealAndCheckGroup(map);
            }
        }
    }

    //将图片名称保存到Redis
    private void savePic2Redis(String pic){
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);
    }
    //将原先图片名称从Redis删除
    private void deletePic2Redis(String pic){
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);
    }
}
