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

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
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

    private static final Log log = LogFactory.getLog(UploadUtil.class);

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

    /**
     * Writes uploaded data into the file specified by path.
     * 
     * @param path
     *            the file path
     * @param formFile
     *            uploaded data
     */
    public static void write(String path, FormFile formFile) {
        if (formFile == null || formFile.getFileSize() == 0) {
            return;
        }
        BufferedOutputStream out = null;
        InputStream in = null;
        try {
            in = formFile.getInputStream();
            out = new BufferedOutputStream(new FileOutputStream(path));
            byte[] buf = new byte[8192];
            int n = 0;
            while ((n = in.read(buf, 0, buf.length)) != -1) {
                out.write(buf, 0, n);
            }
            out.flush();
            formFile.destroy();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            formFile.destroy();
        }
    }
}
