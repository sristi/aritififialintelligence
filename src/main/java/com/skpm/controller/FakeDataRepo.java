package com.skpm.controller;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

import com.skpm.model.FakeModel;

/**
 * @author suresh
 *
 */
public class FakeDataRepo {

	@Produces @Dependent
	List<FakeModel> getFakeData()
	{
		List<FakeModel> dummyList=new ArrayList<FakeModel>();
		FakeModel fakeModel=null;
		for(int i=0;i<=10;i++)
		{
			fakeModel=new FakeModel();
			fakeModel.setSn(i);
			fakeModel.setName("Student No."+i);
			fakeModel.setAddress("Address "+i);
			fakeModel.setRegistrationcode(String.valueOf(10+i));
			fakeModel.setCollegecode("College No.C000"+i);
			fakeModel.setLevel("Level="+i);
			fakeModel.setContact("CONTACT-"+i);
			dummyList.add(fakeModel);
		}
		return dummyList;
	}
}
