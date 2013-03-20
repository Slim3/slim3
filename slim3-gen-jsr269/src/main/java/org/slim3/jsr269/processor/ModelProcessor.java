/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.slim3.jsr269.processor;

import static javax.lang.model.util.ElementFilter.*;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.slim3.datastore.Model;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.message.MessageFormatter;
import org.slim3.gen.processor.AptException;

/**
 * Represents a {@code ModelProcessor} factory.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("org.slim3.datastore.Model")
public class ModelProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Logger.init(processingEnv.getMessager());

        Logger.debug("init ModelProcessor");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {
        for (Element element : typesIn(roundEnv
            .getElementsAnnotatedWith(Model.class))) {
            try {
                System.out.println(element.toString());
            } catch (AptException e) {
                e.sendError();
            } catch (RuntimeException e) {
                Logger.error(element, MessageFormatter.getMessage(
                    MessageCode.SLIM3GEN0001,
                    Model.class.getCanonicalName()));
                throw e;
            }
        }
        return true;
    }
}
