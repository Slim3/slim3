package org.slim3.json.test;


import org.junit.Test;
import org.slim3.json.test.AnnotatedModel;
import org.slim3.json.test.annotation.AnnotatedModelMeta;

public class AnnotationTest {
	@Test
	public void modelToJson() throws Exception{
		AnnotatedModel m = new AnnotatedModel();
		m.setStringAttr("hello");
		
		System.out.println(AnnotatedModelMeta.get().modelToJson(m));
	}
}
