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
package org.slim3.struts.upload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.upload.CommonsMultipartRequestHandler;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;
import org.apache.struts.upload.MultipartRequestWrapper;
import org.slim3.struts.S3StrutsGlobals;
import org.slim3.struts.util.S3ModuleConfigUtil;

/**
 * {@link MultipartRequestHandler} of Slim3.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class S3MultipartRequestHandler extends CommonsMultipartRequestHandler {

    /**
     * The logger.
     */
    protected static Logger logger = Logger
            .getLogger(S3MultipartRequestHandler.class.getName());

    /**
     * The all parameters.
     */
    protected Hashtable<String, Object> elementsAll;

    /**
     * The file parameters.
     */
    protected Hashtable<String, FormFile> elementsFile;

    /**
     * The text parameters.
     */
    protected Hashtable<String, String[]> elementsText;

    @Override
    public void handleRequest(HttpServletRequest request)
            throws ServletException {
        ModuleConfig ac = S3ModuleConfigUtil.getModuleConfig();
        ServletFileUpload upload = new ServletFileUpload();
        upload.setHeaderEncoding(request.getCharacterEncoding());
        upload.setSizeMax(getSizeMax(ac));
        elementsText = new Hashtable<String, String[]>();
        elementsFile = new Hashtable<String, FormFile>();
        elementsAll = new Hashtable<String, Object>();
        try {
            upload = new ServletFileUpload();
            FileItemIterator iter = upload.getItemIterator(request);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                if (item.isFormField()) {
                    addTextParameter(request, item);
                } else {
                    addFileParameter(item);
                }
            }
        } catch (IOException e) {
            log.error("Failed to parse multipart request", e);
            throw new ServletException(e);
        } catch (SizeLimitExceededException e) {
            request.setAttribute(
                    MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED,
                    Boolean.TRUE);
            request.setAttribute(S3StrutsGlobals.SIZE_EXCEPTION_KEY, e);
            return;
        } catch (FileUploadException e) {
            log.error("Failed to parse multipart request", e);
            throw new ServletException(e);
        }
    }

    @Override
    public Hashtable<String, String[]> getTextElements() {
        return elementsText;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Hashtable getFileElements() {
        return elementsFile;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Hashtable getAllElements() {
        return elementsAll;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void rollback() {
        Iterator iter = elementsFile.values().iterator();
        while (iter.hasNext()) {
            FormFile formFile = (FormFile) iter.next();
            formFile.destroy();
        }
    }

    /**
     * Adds text parameter.
     * 
     * @param request
     *            the request
     * @param item
     *            the file item stream
     * @throws IOException
     *             if {@link IOException} has occurred
     */
    protected void addTextParameter(HttpServletRequest request,
            FileItemStream item) throws IOException {
        String name = item.getFieldName();
        String value = null;
        String encoding = request.getCharacterEncoding();
        value = Streams.asString(item.openStream(), encoding);
        if (request instanceof MultipartRequestWrapper) {
            MultipartRequestWrapper wrapper = (MultipartRequestWrapper) request;
            wrapper.setParameter(name, value);
        }
        String[] oldArray = elementsText.get(name);
        String[] newArray;
        if (oldArray != null) {
            newArray = new String[oldArray.length + 1];
            System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
            newArray[oldArray.length] = value;
        } else {
            newArray = new String[] { value };
        }
        elementsText.put(name, newArray);
        elementsAll.put(name, newArray);
    }

    /**
     * Adds form file parameter.
     * 
     * @param item
     *            the file item stream
     * @throws IOException
     *             if {@link IOException} has occurred
     */
    protected void addFileParameter(FileItemStream item) throws IOException {
        FormFile formFile = new S3FormFile(item);
        elementsFile.put(item.getFieldName(), formFile);
        elementsAll.put(item.getFieldName(), formFile);
    }

    /**
     * {@link FormFile} of Slim3.
     * 
     */
    protected static class S3FormFile implements FormFile, Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * The content type.
         */
        protected String contentType;

        /**
         * The file name.
         */
        protected String fileName;

        /**
         * The file data.
         */
        protected byte[] fileData;

        /**
         * Constructor
         * 
         * @param item
         *            the file item stream
         * @throws IOException
         *             if {@link IOException} has occurred
         */
        public S3FormFile(FileItemStream item) throws IOException {
            contentType = item.getContentType();
            fileName = getBaseFileName(item.getName());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Streams.copy(item.openStream(), out, true);
            fileData = out.toByteArray();
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            throw new UnsupportedOperationException(
                    "The setContentType() method is not supported.");
        }

        public int getFileSize() {
            return fileData.length;
        }

        public void setFileSize(int filesize) {
            throw new UnsupportedOperationException(
                    "The setFileSize() method is not supported.");
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            throw new UnsupportedOperationException(
                    "The setFileName() method is not supported.");
        }

        public byte[] getFileData() throws FileNotFoundException, IOException {
            return fileData;
        }

        public InputStream getInputStream() throws FileNotFoundException,
                IOException {
            return new ByteArrayInputStream(fileData);
        }

        public void destroy() {
            contentType = null;
            fileName = null;
            fileData = null;
        }

        /**
         * Returns the base file name.
         * 
         * @param filePath
         *            the file path
         * @return base file name
         */
        protected String getBaseFileName(String filePath) {

            // First, ask the JDK for the base file name.
            String fileName = new File(filePath).getName();

            // Now check for a Windows file name parsed incorrectly.
            int colonIndex = fileName.indexOf(":");
            if (colonIndex == -1) {
                // Check for a Windows SMB file path.
                colonIndex = fileName.indexOf("\\\\");
            }
            int backslashIndex = fileName.lastIndexOf("\\");

            if (colonIndex > -1 && backslashIndex > -1) {
                // Consider this filename to be a full Windows path, and parse
                // it
                // accordingly to retrieve just the base file name.
                fileName = fileName.substring(backslashIndex + 1);
            }

            return fileName;
        }

        @Override
        public String toString() {
            return getFileName();
        }
    }
}