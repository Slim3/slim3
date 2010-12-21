package org.slim3.json.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import net.arnx.jsonic.JSON;

import org.junit.Assert;
import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.json.test.OtherJavaTypeListAttrsModel;
import org.slim3.json.test.OtherJavaTypeListAttrsModel.WeekDay;
import org.slim3.json.test.meta.OtherJavaTypeListAttrsModelMeta;

public class OtherJavaTypeListAttrsModelTest {
	@Test
	public void modelToJson() throws Exception{
		Datastore.setGlobalCipherKey("0654813216578941");
		OtherJavaTypeListAttrsModel m = new OtherJavaTypeListAttrsModel();
		m.setStringListAttr(Arrays.asList("hello", "world"));
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		m.setDateListAttr(Arrays.asList(
				f.parse("2010-11-11 11:11:11")
				, f.parse("2010-12-12 12:12:12")
				));
		m.setEnumListAttr(Arrays.asList(WeekDay.Sun, WeekDay.Mon, WeekDay.Tue));

		String json = OtherJavaTypeListAttrsModelMeta.get().modelToJson(m);
		System.out.println(json);
		JSON j = new JSON();
		j.setSuppressNull(true);
		System.out.println(j.format(m));

		Assert.assertEquals(
				"{\"dateListAttr\":[1289441471000,1292080332000]" +
				",\"enumListAttr\":[\"Sun\",\"Mon\",\"Tue\"]" +
				",\"stringListAttr\":[\"hello\",\"world\"]}"
				, json
				);
	}

	@Test
	public void modelToJson_null(){
		OtherJavaTypeListAttrsModel t = new OtherJavaTypeListAttrsModel();
		String json = OtherJavaTypeListAttrsModelMeta.get().modelToJson(t);
		Assert.assertEquals("{}", json);
	}

	@Test
	public void jsonToModel(){
		OtherJavaTypeListAttrsModel m = OtherJavaTypeListAttrsModelMeta.get().jsonToModel(
			"{\"dateListAttr\":[1289441471000,1292080332000]" +
			",\"enumListAttr\":[\"Sun\",\"Mon\",\"Tue\"]" +
			",\"stringListAttr\":[\"hello\",\"world\"]}"
			);
		Assert.assertArrayEquals(
				Arrays.asList(new Date(1289441471000L), new Date(1292080332000L)).toArray()
				, m.getDateListAttr().toArray());
		Assert.assertArrayEquals(
				Arrays.asList(WeekDay.Sun, WeekDay.Mon, WeekDay.Tue).toArray()
				, m.getEnumListAttr().toArray());
		Assert.assertArrayEquals(
				Arrays.asList("hello", "world").toArray()
				, m.getStringListAttr().toArray());
	}
}
