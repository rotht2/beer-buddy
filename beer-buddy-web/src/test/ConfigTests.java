package com.beerbuddy;


import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import com.beerbuddy.core.config.CoreConfig;
import com.beerbuddy.core.model.Beer;
import com.beerbuddy.core.repository.BeerRepository;
import com.beerbuddy.core.repository.BeerSyncRepository;
import com.beerbuddy.core.repository.UserBeerRankRepository;
import com.beerbuddy.core.repository.UserProfileRepository;
import com.beerbuddy.core.repository.UserRepository;
import com.beerbuddy.core.service.BeerStoreSyncService;
import com.beerbuddy.core.service.UserService;
import com.beerbuddy.core.service.impl.BeerStoreSyncServiceMonitor;
import com.beerbuddy.core.service.impl.DefaultBeerStoreSyncService;
import com.beerbuddy.core.service.impl.DefaultUserService;
import com.beerbuddy.web.config.AppConfig;
import com.beerbuddy.web.controller.ui.model.BeerDTO;
import com.beerbuddy.web.listener.BeerStoreSyncListener;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;


public class ConfigTests
{
	@Mock
	Environment e;
	
	@Mock
	BeerStoreSyncSercvice bs;
	
	Beer newBeer = new Beer();
	
	@Before
	public void startup()
	{
		newBeer.setAbv("5.5");
		newBeer.setBeerStoreId(12);
		newBeer.setBrewer("Molson");
		newBeer.setCategory("Craft");
		newBeer.setCountry("Canada, eh?");
		newBeer.setId((long)1112223334);
		newBeer.setImageUrl("Image");
		newBeer.setName("BuddyLight");
		newBeer.setOnSale(true);
		newBeer.setType("Lager");
	}
	
	@Test
	public void BeerStoreSyncListenerTest()
	{
		BeerStoreSyncListener b = new BeerStoreSyncListener(e, bs);
		
		MockitoAnnotations.initMocks(this);
		
		when(e.getProperty(anyString(), anyString())).thenReturn(true);
		when(bs.sync()).thenReturn(true);
		b.runSync();
	}
	
	@Test
	public void BeerDTOTest()
	{
		BeerDTO dto = new BeerDTO();
		dto.setAbv(newBeer.getAbv());
		dto.setBrewer(newBeer.getBrewer());
		dto.setCategory(newBeer.getCategory());
		dto.setCountry(newBeer.getCountry());
		dto.setId(newBeer.getId());
		dto.setImageUrl(newBeer.getImageUrl());
		dto.setName(newBeer.getName());
		dto.setOnSale(newBeer.isOnSale());
		dto.setType(newBeer.getType());
		
		assertThat(dto.getAbv(), is(equalTo(newBeer.getAbv()));
		assertThat(dto.getBrewer(), is(equalTo(newBeer.getBrewer()));
		assertThat(dto.getCategory(), is(equalTo(newBeer.getCategory()));
		assertThat(dto.getCountry(), is(equalTo(newBeer.getCountry()));
		assertThat(dto.getId(), is(equalTo(newBeer.getId()));
		assertThat(dto.getImageUrl(), is(equalTo(newBeer.getImageUrl()));
		assertThat(dto.getName(), is(equalTo(newBeer.getName()));
		assertThat(dto.isOnSale(), is(equalTo(newBeer.isOnSale()));
		assertThat(dto.getType(), is(equalTo(newBeer.getType()));
		
		
	}
}