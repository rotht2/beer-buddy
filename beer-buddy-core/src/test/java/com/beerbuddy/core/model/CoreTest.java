package com.beerbuddy.core.model;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Date;

import com.beerbuddy.core.model.DefaultUser;
import com.beerbuddy.core.model.UnsecureUserWrapper;
import com.beerbuddy.core.repository.BeerRepository;
import com.beerbuddy.core.repository.BeerSyncRepository;
import com.beerbuddy.core.repository.UserRepository;
import com.beerbuddy.core.security.BeerBuddyAuthenticationManager;
import com.beerbuddy.core.security.BeerBuddyUserDetailsService;
import com.beerbuddy.core.service.BeerStoreSyncService;
import com.beerbuddy.core.service.impl.BeerStoreSyncServiceMonitor;


public class CoreTest{
	
	
	@Mock
	UserRepository userRepo;
	
	@Mock
	BeerStoreSyncService BeerStore;
	
	@Mock
	BeerSyncRepository BeerSyncRepo;
	
	@Mock
	BeerRepository BeerRepo;
	
	@Mock
	RestTemplate restTemplate;
	
	Beer newBeer = new Beer();
	UserProfile profile = new UserProfile();
	
	@Before
	public void setup()
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
		
		profile.setEmail("newguy@fake.com");
		profile.setId(1112223334);
		profile.setName("Newbie");
		
	}
	@Test
	public void BeerTest()
	{
		String abv = "5.5";
		int storeID = 12;
		String brewer = "Molson";
		String category = "Craft";
		String country = "Canada, eh?";
		long id = 1112223334;
		String imageUrl = "Image";
		String name = "BuddyLight";
		String type = "Lager";
		assertThat(newBeer.getAbv(), is(equalTo(abv)));
		assertThat(newBeer.getBeerStoreId(), is(equalTo((long)storeID)));
		assertThat(newBeer.getName(), is(equalTo(name)));
		assertThat(newBeer.getType(), is(equalTo(type)));
		assertThat(newBeer.getBrewer(), is(equalTo(brewer)));
		assertThat(newBeer.getCountry(), is(equalTo(country)));
		assertThat(newBeer.getId(), is(equalTo(id)));
		assertThat(newBeer.getImageUrl(), is(equalTo(imageUrl)));
		assertThat(newBeer.isOnSale(), is(equalTo(true)));
		assertThat(newBeer.getCategory(), is(equalTo(category)));
		UserBeerRank ranking = new UserBeerRank();
		Set<UserBeerRank> set = new HashSet<UserBeerRank>();
		newBeer.setUserBeerRankings(set);
		newBeer.getUserBeerRankings().add(ranking);
		assertThat(newBeer.getUserBeerRankings().containsAll(set), is(equalTo(true)));
	}
	@Test
	public void BeerSyncTest()
	{
		BeerSync b = new BeerSync();
		long id = 1112223334;
		b.setId(id);
		assertThat(b.getId(), is(equalTo(id)));
		b.setStatus(SyncStatus.STARTED);
		assertThat(b.getStatus(),is(equalTo(SyncStatus.STARTED)));
		b.setStatus(SyncStatus.ERRORED);
		b.setStatus(SyncStatus.ENDED);
		Date d = new Date();
		b.setTimestamp(d);
		assertThat(b.getTimestamp(), is(equalTo(d)));
	}
	
	@Test
	public void UserTest()
	{
		
		//Testing profile
		assertThat(profile.getEmail(), is(equalTo("newguy@fake.com")));
		assertThat(profile.getId(), is(equalTo((long)1112223334)));
		assertThat(profile.getId(), is(equalTo(profile.getProfileId())));
		assertThat(profile.getName(), is(equalTo("Newbie")));
		
		
		//Testing DefaultUser
		DefaultUser d = new DefaultUser();
		d.setProfile(profile);
		profile.setUser(d);
		d.setUsername("BigDrinker24");
		d.setPassword("WHYPHY");
		d.setLastLogin(new Date(999999999));
		d.setSalt("salt");
		assertThat(d.getEmail(), is(equalTo(profile.getEmail())));
		assertThat(d.getName(), is(equalTo(profile.getName())));
		assertThat(d.getProfileId(), is(equalTo(profile.getId())));
		assertThat(d.getProfile(), is(equalTo(profile)));
		assertThat(profile.getUsername(), is(equalTo("BigDrinker24")));
		assertThat(profile.getLastLogin(), is(equalTo(new Date(999999999))));
		
		
		DefaultUser def = new DefaultUser();
		assertThat(def.getProfileId(), is(equalTo(null)));
		
		//Testing UserProfile.setProfile(UserProfile)
		UserProfile p = new UserProfile();
		p.setProfile(profile);
		assertThat(p.getEmail(), is(equalTo("newguy@fake.com")));
		assertThat(p.getId(), is(equalTo((long)1112223334)));
		assertThat(p.getId(), is(equalTo(p.getProfileId())));
		
		UserProfile u = new UserProfile();
		assertThat(u.getUsername(), is(equalTo(null)));
		assertThat(u.getLastLogin(), is(equalTo(null)));
		
		//Checking when you set UserProfile's user to a wrapper
		UserWrapper wrapper = new UserWrapper(d);
		p.setUser(wrapper);
		assertThat(p.getUsername(), is(equalTo(d.getUsername())));
		
		//Checking when you set UserProfile's user to a null wrapper
		UserWrapper v = new UserWrapper(null);
		p.setUser(v);
		assertThat(p.getUsername(), is(equalTo(d.getUsername())));
		
		UnsecureUserWrapper uns = new UnsecureUserWrapper(d);
		UnsecureUserWrapper uns2 = new UnsecureUserWrapper(wrapper);
		assertThat(uns.getUser(),is(equalTo(d)));
		assertThat(uns2.getUser(),is(equalTo(wrapper.user)));
		
		UserWrapper wrap = new UserWrapper(d);
		UserWrapper wrap2 = new UserWrapper(new DefaultUser());
		assertThat(wrap2.getLastLoginInMillis(),is(equalTo((long)-1)));
		assertThat(wrap.getLastLoginInMillis(),is(d.getLastLogin().getTime()));
		
		wrap.setProfile(profile);
		assertThat(wrap.getProfileId(), is(equalTo(profile.getId())));
		
		
	}
	
	@Test
	public void UserBeerRankTest()
	{
		UserBeerRank ranking = new UserBeerRank();
		long id = 1112223334;
		int rank = 5;
		ranking.setBeer(newBeer);
		ranking.setId(id);
		ranking.setRank(rank);
		ranking.setUser(profile);
		assertThat(ranking.getBeer(), is(equalTo(newBeer)));
		assertThat(ranking.getId(), is(equalTo(id)));
		assertThat(ranking.getRank(), is(equalTo(rank)));
		assertThat(ranking.getUser(), is(equalTo(profile)));
	}
	
	@Test
	public void securityTest()
	{
		DefaultUser d = new DefaultUser();
		d.setUsername("bigDrinker24");
		d.setPassword("WHYPHY");
		d.setLastLogin(new Date(999999999));
		d.setSalt("Pepper!");	
		MockitoAnnotations.initMocks(this);
		when(userRepo.findByUsername("bigDrinker24")).thenReturn(d);
		when(userRepo.findByUsernameAndPassword(anyString(),anyString())).thenReturn(d);
		BeerBuddyAuthenticationManager a = new BeerBuddyAuthenticationManager(userRepo);
		UsernamePasswordAuthenticationToken u = new UsernamePasswordAuthenticationToken("bigDrinker24", "WHYPHY");
		Authentication verify = a.authenticate(u);
		assertThat(verify.getName(), is(equalTo("bigDrinker24")));
		
	}
	
	@Test (expected=UsernameNotFoundException.class)
	public void authenticationExceptionsTest()
	{
		MockitoAnnotations.initMocks(this);
		when(userRepo.findByUsername("bigDrinker24")).thenReturn(null);
		BeerBuddyAuthenticationManager a = new BeerBuddyAuthenticationManager(userRepo);
		UsernamePasswordAuthenticationToken u = new UsernamePasswordAuthenticationToken("bigDrinker24", "WHYPHY");
		Authentication verify = a.authenticate(u);
	}
	
	@Test (expected=BadCredentialsException.class)
	public void authenticationExceptionsTestTwo()
	{
		DefaultUser d = new DefaultUser();
		d.setUsername("bigDrinker24");
		d.setPassword("WHYPHY");
		d.setLastLogin(new Date(999999999));
		d.setSalt("Pepper!");	
		MockitoAnnotations.initMocks(this);
		when(userRepo.findByUsername("bigDrinker24")).thenReturn(d);
		when(userRepo.findByUsernameAndPassword(anyString(),anyString())).thenReturn(null);
		BeerBuddyAuthenticationManager a = new BeerBuddyAuthenticationManager(userRepo);
		UsernamePasswordAuthenticationToken u = new UsernamePasswordAuthenticationToken("bigDrinker24", "WHYPHY");
		Authentication verify = a.authenticate(u);
	}
	
	@Test (expected=InsufficientAuthenticationException.class)
	public void authenticationExceptionsTestThree()
	{
		DefaultUser d = new DefaultUser();
		d.setUsername("bigDrinker24");
		d.setPassword("WHYPHY");
		d.setLastLogin(new Date(999999999));
		d.setSalt("Pepper!");	
		MockitoAnnotations.initMocks(this);
		when(userRepo.findByUsername("bigDrinker24")).thenReturn(d);
		when(userRepo.findByUsernameAndPassword(anyString(),anyString())).thenReturn(d);
		BeerBuddyAuthenticationManager a = new BeerBuddyAuthenticationManager(userRepo);
		Authentication u = new TestingAuthenticationToken("newbie","newbie2");
		Authentication verify = a.authenticate(u);
	}
	
	@Test
	public void BeerBuddyUserDetailsServiceTest()
	{
		BeerBuddyUserDetailsService u = new BeerBuddyUserDetailsService(userRepo);
		assertThat(u.loadUserByUsername("bigDrinker24"), is(equalTo(null)));
	}
	
	@Test
	public void BeerStoreSyncServiceMonitorTest()
	{
		MockitoAnnotations.initMocks(this);
		when(BeerStore.sync()).thenReturn(true);
		BeerStoreSyncServiceMonitor monitor = new BeerStoreSyncServiceMonitor(BeerStore, BeerSyncRepo);
		boolean result = monitor.sync();
		assertThat(result, is(equalTo(true)));
	}
	
	@Test
	public void BeerStoreSyncServiceMonitorTestTwo()
	{
		MockitoAnnotations.initMocks(this);
		when(BeerStore.sync()).thenThrow(new NullPointerException());
		BeerStoreSyncServiceMonitor monitor = new BeerStoreSyncServiceMonitor(BeerStore, BeerSyncRepo);
		boolean result = monitor.sync();
		assertThat(result, is(equalTo(false)));	
	}
}