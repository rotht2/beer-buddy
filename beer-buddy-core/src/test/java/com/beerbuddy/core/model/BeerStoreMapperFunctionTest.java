package com.beerbuddy.core.model;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.beerbuddy.core.function.BeerStoreMapperFunction;

public abstract class BeerStoreMapperFunctionTest{
		public abstract BeerStoreMapperFunction createInstance();
		
		@Test
		public final void testApply()
		{
			BeerStoreMapperFunction instance = createInstance();
			Map<String, Object> json = new HashMap<String, Object>();
			json.put("name", "Rickards Cardigan");
			json.put("beer_id" , 120);
			json.put("image_url" , "http://www.thebeerstore.ca/sites/default/files/styles/brand_hero/public/brand/hero/Rickards%20Cardigan%20Hero.jpg?itok=IRGzYwK0");
			json.put("category" , "Craft");
			json.put("abv" , "5.5");
			json.put("type" , "Lager");
			json.put("brewer" , "Molson");
			json.put("country" , "Canada");
			json.put("on_sale" , false);
			Beer b = instance.apply(json);
			assertThat(b.getName(), is(equalTo("Rickards Cardigan")));
		}
	}