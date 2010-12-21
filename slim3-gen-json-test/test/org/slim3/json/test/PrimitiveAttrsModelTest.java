package org.slim3.json.test;

import net.arnx.jsonic.JSON;

import org.junit.Assert;
import org.junit.Test;
import org.slim3.json.test.PrimitiveAttrsModel;
import org.slim3.json.test.meta.PrimitiveAttrsModelMeta;

public class PrimitiveAttrsModelTest {
	@Test
	public void modelToJson() throws Exception{
		PrimitiveAttrsModel m = new PrimitiveAttrsModel();
		m.setBooleanAttr(true);
		m.setShortAttr((short)100);
		m.setIntAttr(1000);
		m.setLongAttr(10000);
		m.setFloatAttr(1.1f);
		m.setDoubleAttr(11.1);
		String json = PrimitiveAttrsModelMeta.get().modelToJson(m);
		System.out.println(json);
		JSON j = new JSON();
		j.setSuppressNull(true);
		System.out.println(j.format(m));
		Assert.assertEquals(
				"{\"booleanAttr\":true,\"doubleAttr\":11.1" +
				",\"floatAttr\":1.1,\"intAttr\":1000" +
				",\"longAttr\":10000,\"shortAttr\":100}"
				, json
				);
	}

	@Test
	public void jsonToModel() throws Exception{
		PrimitiveAttrsModel m = PrimitiveAttrsModelMeta.get().jsonToModel(
				"{\"booleanAttr\":true,\"doubleAttr\":11.1" +
				",\"floatAttr\":1.1,\"intAttr\":1000" +
				",\"longAttr\":10000,\"shortAttr\":100}"
				);
		Assert.assertEquals(true, m.isBooleanAttr());
		Assert.assertEquals(11.1, m.getDoubleAttr(), 0.1);
		Assert.assertEquals(1.1, m.getFloatAttr(), 0.1);
		Assert.assertEquals(1000, m.getIntAttr());
		Assert.assertEquals(10000, m.getLongAttr());
		Assert.assertEquals(100, m.getShortAttr());
	}

	@Test
	public void jsonToModel_null() throws Exception{
		PrimitiveAttrsModel m = PrimitiveAttrsModelMeta.get().jsonToModel(
				"{}"
				);
		Assert.assertEquals(false, m.isBooleanAttr());
		Assert.assertEquals(0, m.getDoubleAttr(), 0.1);
		Assert.assertEquals(0, m.getFloatAttr(), 0.1);
		Assert.assertEquals(0, m.getIntAttr());
		Assert.assertEquals(0, m.getLongAttr());
		Assert.assertEquals(0, m.getShortAttr());
	}

	@Test
	public void jsonToModel_intOnly() throws Exception{
		PrimitiveAttrsModel m = PrimitiveAttrsModelMeta.get().jsonToModel(
				"{\"intAttr\":20}"
				);
		Assert.assertEquals(false, m.isBooleanAttr());
		Assert.assertEquals(0, m.getDoubleAttr(), 0.1);
		Assert.assertEquals(0, m.getFloatAttr(), 0.1);
		Assert.assertEquals(20, m.getIntAttr());
		Assert.assertEquals(0, m.getLongAttr());
		Assert.assertEquals(0, m.getShortAttr());
	}

	@Test
	public void modelToJsonToModel() throws Exception{
		String json = null;
		{
			PrimitiveAttrsModel m = new PrimitiveAttrsModel();
			m.setBooleanAttr(true);
			m.setShortAttr((short)100);
			m.setIntAttr(1000);
			m.setLongAttr(10000);
			m.setFloatAttr(1.1f);
			m.setDoubleAttr(11.1);
			json = PrimitiveAttrsModelMeta.get().modelToJson(m);
		}

		PrimitiveAttrsModel m = PrimitiveAttrsModelMeta.get().jsonToModel(
				json);
		Assert.assertEquals(true, m.isBooleanAttr());
		Assert.assertEquals(11.1, m.getDoubleAttr(), 0.1);
		Assert.assertEquals(1.1, m.getFloatAttr(), 0.1);
		Assert.assertEquals(1000, m.getIntAttr());
		Assert.assertEquals(10000, m.getLongAttr());
		Assert.assertEquals(100, m.getShortAttr());
	}
}
