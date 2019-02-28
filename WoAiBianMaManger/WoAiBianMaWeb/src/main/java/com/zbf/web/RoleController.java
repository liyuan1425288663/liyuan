package com.zbf.web;

import com.zbf.common.ResponseResult;
import com.zbf.core.CommonUtils;
import com.zbf.core.page.Page;
import com.zbf.core.utils.UID;
import com.zbf.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 作者：LCG
 * 创建时间：2019/1/31 19:35
 * 描述：角色列表
 */
@RequestMapping("/role")
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 获取角色列表
     * @param request
     * @return
     */
    @RequestMapping("getRolelistPage")
    public ResponseResult getRolelistPage(HttpServletRequest request){

        Map<String, Object> paramsJsonMap = CommonUtils.getParamsJsonMap ( request );
        ResponseResult responseResult=ResponseResult.getResponseResult ();
        Page<Map<String,Object>> page=new Page<> ();
        page.setParams ( paramsJsonMap );
        Page.setPageInfo ( page,paramsJsonMap );
        //查询数据
        roleService.getRolePage ( page );
        responseResult.setResult ( page );

        return responseResult;
    }

    /**
     * 删除一个角色
     * @param request
     * @return
     */
    @RequestMapping("deleteRole")
    public ResponseResult deleteRole(HttpServletRequest request){
        //获取参数
        Map<String, Object> paramsJsonMap = CommonUtils.getParamsJsonMap ( request );
        //获取要删除的角色的ID
        ResponseResult responseResult=ResponseResult.getResponseResult ();

        roleService.deleteByRoleId ( paramsJsonMap );

        responseResult.setSuccess ( "ok" );

        return responseResult;
    }


    /**
     * 新增加一个角色
     * @return
     */
    @RequestMapping("toAddRole")
    public ResponseResult toAddRole(HttpServletRequest request){

        ResponseResult responseResult=ResponseResult.getResponseResult ();

        Map<String, Object> paramsJsonMap = CommonUtils.getParamsJsonMap ( request );

        paramsJsonMap.put ( "id", UID.next () );

        roleService.addRole ( paramsJsonMap );

        responseResult.setSuccess ( "ok" );

        return responseResult;
    }

    /**
     * 编辑角色和菜单
     * @param request
     * @return
     */
    @RequestMapping("eidtRoleMenu")
    public ResponseResult eidtRoleMenu(HttpServletRequest request){

        ResponseResult responseResult=ResponseResult.getResponseResult ();
        Map<String, Object> paramsJsonMap = CommonUtils.getParamsJsonMap ( request );

        if(paramsJsonMap.get ( "menuIds" )!=null&&paramsJsonMap.get ( "menuIds" ).toString ().length ()>0){
            paramsJsonMap.put ( "menuIds",Arrays.asList ( paramsJsonMap.get ( "menuIds" ).toString ().split ( ":" ) ) );
        };
        roleService.updateRole ( paramsJsonMap );

        responseResult.setSuccess ( "ok" );

        return responseResult;
    }

    @RequestMapping("getRoleListByQuery")
    public ResponseResult getRoleListByQuery(HttpServletRequest request){

        ResponseResult responseResult=ResponseResult.getResponseResult ();
        Map<String, Object> paramsJsonMap = CommonUtils.getParamsJsonMap ( request );

        List<Map<String, Object>> roleListByQuery = roleService.getRoleListByQuery ( paramsJsonMap );

        responseResult.setResult ( roleListByQuery );

        return responseResult;
    }

}
