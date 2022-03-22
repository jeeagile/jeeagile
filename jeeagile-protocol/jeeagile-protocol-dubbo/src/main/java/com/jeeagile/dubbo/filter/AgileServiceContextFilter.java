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
@Activate(group = {CommonConstants.PROVIDER})
public class AgileServiceContextFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            AgileBaseUser userData = (AgileBaseUser) invocation.getObjectAttachment(AgileConstants.AGILE_USER_DATA);
            if (userData != null && AgileStringUtil.isNotEmpty(userData.getUserId())) {
                AgileSecurityContext.putCurrentUser(userData);
            }
            return invoker.invoke(invocation);
        } finally {
            AgileSecurityContext.removeCurrentUser();
        }

    }
}
