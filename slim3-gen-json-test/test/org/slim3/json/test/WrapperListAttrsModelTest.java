package org.slim3.json.test;

import java.util.Arrays;

import net.arnx.jsonic.JSON;

import org.junit.Assert;
import org.junit.Test;
import org.slim3.json.test.WrapperListAttrsModel;
import org.slim3.json.test.meta.WrapperListAttrsModelMeta;

public class WrapperListAttrsModelTest {
	@Test
	public void modelToJson(){
		WrapperListAttrsModelMeta m = WrapperListAttrsModelMeta.get();
		WrapperListAttrsModel model = new WrapperListAttrsModel();
		model.setBooleanListAttr(Arrays.asList(true, false, true));
		model.setShortListAttr(Arrays.asList((short)9, (short)8, (short)7));
		model.setIntegerListAttr(Arrays.asList(9, 8, 7));
		model.setLongListAttr(Arrays.asList(9L, 8L, 7L));
		model.setFloatListAttr(Arrays.asList(9.9f, 8.8f, 7.7f));
		model.setDoubleListAttr(Arrays.asList(9.9, 8.8, 7.7));
		String json = m.modelToJson(model);
		System.out.println(json);
		JSON j = new JSON();
		j.setSuppressNull(true);
		System.out.println(j.format(model));
		Assert.assertEquals(
				"{\"booleanListAttr\":[true,false,true],\"doubleListAttr\":[9.9,8.8,7.7],\"floatListAttr\":[9.9,8.8,7.7]" +
				",\"integerListAttr\":[9,8,7],\"longListAttr\":[9,8,7]" +
				",\"shortListAttr\":[9,8,7]}"
				, json);
	}

	@Test
	public void modelToJson_null() throws Exception{
		WrapperListAttrsModel m = new WrapperListAttrsModel();
		String json = WrapperListAttrsModelMeta.get().modelToJson(m);
		Assert.assertEquals("{}", json);
	}

	@Test
	public void jsonToModel() throws Exception{
		WrapperListAttrsModel m = WrapperListAttrsModelMeta.get().jsonToModel(
				"{\"booleanListAttr\":[true,false,true],\"doubleListAttr\":[9.9,8.8,7.7]" +
				",\"floatListAttr\":[9.9,8.8,7.7]" +
				",\"integerListAttr\":[9,8,7],\"longListAttr\":[9,8,7]" +
				",\"shortListAttr\":[9,8,7]}");
		Assert.assertArrayEquals(
				Arrays.asList(true, false, true).toArray()
				, m.getBooleanListAttr().toArray());
		Assert.assertArrayEquals(
				Arrays.asList(9.9, 8.8, 7.7).toArray()
				, m.getDoubleListAttr().toArray());
		Assert.assertArrayEquals(
				Arrays.asList(9.9f, 8.8f, 7.7f).toArray()
				, m.getFloatListAttr().toArray());
		Assert.assertArrayEquals(
				Arrays.asList(9, 8, 7).toArray()
				, m.getIntegerListAttr().toArray());
		Assert.assertArrayEquals(
				Arrays.asList(9L, 8L, 7L).toArray()
				, m.getLongListAttr().toArray());
		Assert.assertArrayEquals(
				Arrays.asList((short)9, (short)8, (short)7).toArray()
				, m.getShortListAttr().toArray());
	}

	@Test
	public void jsonToModel_invalidValue() throws Exception{
		WrapperListAttrsModel m = WrapperListAttrsModelMeta.get().jsonToModel(
				"{\"integerListAttr\":[1,\"hello\",100]}");
		Assert.assertEquals(2, m.getIntegerListAttr().size());
		Assert.assertEquals(1, (int)m.getIntegerListAttr().get(0));
		Assert.assertEquals(100, (int)m.getIntegerListAttr().get(1));
	}
}
