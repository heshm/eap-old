
package org.eap.oa.act.controller;


import org.eap.framework.domain.AdminUser;
import org.eap.framework.mapper.AdminUserMapper;
import org.eap.framework.web.util.AuthenticationUtils;
import org.eap.oa.flowable.editor.dto.UserRepresentation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping(value = "/oa/act/app")
public class AccountResource {


   /* @Autowired
    protected UserService userService;*/
    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private ObjectMapper objectMapper;
    
    @RequestMapping(value = "/rest/authenticate", method = RequestMethod.GET, produces = {"application/json"})
    public ObjectNode isAuthenticated(HttpServletRequest request) {
        String user = request.getRemoteUser();

        if(user == null) {
            throw new RuntimeException("Request did not contain valid authorization");
        }

        ObjectNode result = objectMapper.createObjectNode();
        result.put("login", user);
        return result;
    }
    
    @RequestMapping(value = "/rest/account",method = RequestMethod.GET,produces = "application/json")
    public UserRepresentation getAccount(HttpServletResponse response) {
    	String userId = AuthenticationUtils.getUserId();
    	AdminUser user = adminUserMapper.selectByPrimaryKey(userId);
    	if(user == null) {
    		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    		return null;
    	}else {
    		return new UserRepresentation(user);
    	}
    }

    /**
     * GET  /rest/account -> get the current user.
     */
    /*@RequestMapping(value = "/rest/account",
            method = RequestMethod.GET,
            produces = "application/json")
    public UserRepresentation getAccount(HttpServletResponse response) {
        AccountRepresentation account = userService.getAccountRepresentation(SecurityUtils.getCurrentLogin());
        if (account == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return account;
    }*/
    
}
