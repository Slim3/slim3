/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.slim3.struts.util;

import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.slim3.struts.S3StrutsGlobals;
import org.slim3.struts.web.WebLocator;

/**
 * A utility for file upload.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class UploadUtil {

    private UploadUtil() {
    }

    /**
     * Checks if the uploaded data do not exceed size limit.
     * 
     * @return whether the uploaded data do not exceed size limit
     */
    public static boolean checkSizeLimit() {
        SizeLimitExceededException e = (SizeLimitExceededException) WebLocator
                .getRequest().getAttribute(S3StrutsGlobals.SIZE_EXCEPTION_KEY);
        if (e != null) {
            ActionMessages errors = new ActionMessages();
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "errors.upload.size", new Object[] { e.getActualSize(),
                            e.getPermittedSize() }));
            ActionMessagesUtil.addErrorsIntoRequest(errors);
            return false;
        }
        return true;
    }
}
