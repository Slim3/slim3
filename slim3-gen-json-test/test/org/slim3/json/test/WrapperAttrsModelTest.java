package org.slim3.json.test;

import net.arnx.jsonic.JSON;

import org.junit.Assert;
import org.junit.Test;
import org.slim3.json.test.WrapperAttrsModel;
import org.slim3.json.test.meta.WrapperAttrsModelMeta;

public class WrapperAttrsModelTest {
	@Test
	public void modelToJson() throws Exception{
		WrapperAttrsModel m = new WrapperAttrsModel();
		m.setBooleanAttr(true);
		m.setShortAttr((short)100);
		m.setIntegerAttr(1000);
		m.setLongAttr(10000L);
		m.setFloatAttr(1.1f);
		m.setDoubleAttr(11.1);
		String json = WrapperAttrsModelMeta.get().modelToJson(m);
		System.out.println(json);
		JSON j = new JSON();
		j.setSuppressNull(true);
		System.out.println(j.format(m));
		Assert.assertEquals(
				"{\"booleanAttr\":true,\"doubleAttr\":11.1" +
				",\"floatAttr\":1.1,\"integerAttr\":1000" +
				",\"longAttr\":10000,\"shortAttr\":100}"
				, json
				);
	}

	@Test
	public void modelToJson_null() throws Exception{
		WrapperAttrsModel m = new WrapperAttrsModel();
		String json = WrapperAttrsModelMeta.get().modelToJson(m);
		Assert.assertEquals("{}", json);
	}

	@Test
	public void modelToJson_short() throws Exception{
		WrapperAttrsModel m = new WrapperAttrsModel();
		m.setShortAttr((short)1);
		String json = WrapperAttrsModelMeta.get().modelToJson(m);
		Assert.assertEquals("{\"shortAttr\":1}", json);
	}

	@Test
	public void jsonToModel(){
		WrapperAttrsModel m = WrapperAttrsModelMeta.get().jsonToModel(
			"{\"booleanAttr\":true,\"doubleAttr\":11.1" +
			",\"floatAttr\":1.1,\"integerAttr\":1000" +
			",\"longAttr\":10000,\"shortAttr\":100}");
		Assert.assertEquals(true, m.getBooleanAttr());
		Assert.assertEquals(11.1, m.getDoubleAttr(), 0.1);
		Assert.assertEquals(1.1, m.getFloatAttr(), 0.1);
		Assert.assertEquals((Integer)1000, m.getIntegerAttr());
		Assert.assertEquals((Long)10000L, m.getLongAttr());
		Assert.assertEquals((Short)(short)100, m.getShortAttr());
	}

	@Test
	public void jsonToModel_notLong(){
		WrapperAttrsModel m = WrapperAttrsModelMeta.get().jsonToModel(
			"{\"longAttr\":true}");
		Assert.assertNull(m.getLongAttr());
	}
}
