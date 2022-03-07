package com.jeeagile.dubbo.filter;

import com.jeeagile.core.constants.AgileConstants;
import com.jeeagile.core.security.context.AgileSecurityContext;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.util.AgileStringUtil;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Activate(group = {CommonConstants.CONSUMER})
public class AgileReferenceContextFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        AgileBaseUser userData = AgileSecurityContext.getCurrentUser();
        if (userData != null && AgileStringUtil.isNotEmpty(userData.getUserId())) {
            invocation.setAttachment(AgileConstants.AGILE_USER_DATA, userData);
        }
        return invoker.invoke(invocation);
    }
}
