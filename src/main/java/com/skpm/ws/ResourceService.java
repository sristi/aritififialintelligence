package com.skpm.ws;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;

import com.skpm.model.FakeModel;

/**
 * @author suresh
 *
 */
@Path("/sayHello")
public class ResourceService {

	@Inject
	List<FakeModel> fakeDataList;
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String ping(){
		return "WE Get our Hello World!";
	}
	@Path("/fakeDataXml")
	@GET
	@Produces(MediaType.TEXT_XML)
	public List<FakeModel> dataXml(){
		return fakeDataList;
	}
	@Path("/fakeDataJson")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@XmlElement(name="FakaDataList")
	public List<FakeModel> dataJson(){
		return fakeDataList;
	}
	@Path("/fakeDataHtml")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public List<FakeModel> dataListJson(){
		return fakeDataList;
	}
	/*
	 * MediaType.APPLICATION_JSON = [all values of objects comma separated]
	 * MediaType.TEXT_PLAIN=java object representation collection
	 * 
	 */
	/**
	 * @return downloadable
	 */
	@Path("/fakeData")
	@GET
	@Produces("application/vnd.test-v1")
	public Response test() {
		return Response.ok("Version 1", "application/vnd.test-v1").build();
	}
	@GET
	@Path("/fakeDataJson/{sn}")
	@Produces(MediaType.APPLICATION_JSON)
	public FakeModel getCluster(@PathParam("sn") int idPassed) {
		return fakeDataList.get(idPassed);
	}
	@GET
	@Path("/clientVerifierDummy/{memberCode}/{branchCode}/{bankCode}")
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean isClientVerified(@PathParam("memberCode") String memberCode,
			@PathParam("branchCode") String branchCode,@PathParam("bankCode") String bankCode) {
		if(!memberCode.isEmpty() && !branchCode.isEmpty()&& !bankCode.isEmpty())
		{
			return Boolean.TRUE;
		}
		else {
			return Boolean.FALSE;
		}
	}
	@GET
	@Path("/balanceDummy/{memberCode}/{branchCode}")
	@Produces(MediaType.APPLICATION_JSON)
	public String balanceQuery(@PathParam("memberCode") Integer memberCode,
			@PathParam("branchCode") String branchCode) {
		String balanceString="MEMBER_CODE:"+memberCode+", CURRENT_BALANCE:";
		if(memberCode>0 && !branchCode.isEmpty())
		{
			//This dummy balance calculation should be altered later by actual business code
			balanceString+=df.format(getRandomBalance(500.00, 50000.00));
		}
		else {
			balanceString+= "0.00";
		}

		//final processing on response string
		return balanceString;
	}
	@GET
	@Path("/dummyBalance/{memberCode}/{schemes: .*}")
	@Produces(MediaType.APPLICATION_JSON)
	public String balanceForSchemes(@PathParam("memberCode") Integer memberCode,
			@PathParam("schemes") String schemesValue) {
		String[] schemes = schemesValue.split(",");
		StringBuilder balanceString= new StringBuilder();
		if(memberCode>0 && (null != schemes && schemes.length>0 ) )
		{
			balanceString.append("MEMBER_CODE:"+memberCode+", CURRENT_BALANCE:"); 
			for(String scheme: schemes)
			{
				//This dummy balance calculation should be altered later by actual business code
				balanceString.append(scheme+":"+df.format(getRandomBalance(500.00, 50000.00)));
			}
		}
		else {
			balanceString.append("0.00");
		}

		//final processing on response string
		return balanceString.toString();
	}
	@GET
	@Path("/meetingDateDummy/{memberCode}/{branchCode}")
	@Produces(MediaType.APPLICATION_JSON)
	public String nextMeetingDateQyery(@PathParam("memberCode") Integer memberCode,
			@PathParam("branchCode") String branchCode) {
		String balanceString="MEMBER_CODE:"+memberCode+", NEXT_MEETING_DATE:";
		if(memberCode>0 && !branchCode.isEmpty())
		{
			//This dummy balance calculation should be altered later by actual business code
			balanceString+=String.valueOf(getRandomDate());
		}
		else {
			balanceString+= "N/A";
		}

		//final processing on response string
		return balanceString;
	}

	@GET
	@Path("/loanStatementDummy/{memberCode}/{branchCode}")
	@Produces(MediaType.APPLICATION_JSON)
	public String loanStatement(@PathParam("memberCode") Integer memberCode,
			@PathParam("branchCode") String branchCode) {
		String retValue="MEMBER_CODE:"+memberCode+", LOAN(S):";
		if(memberCode>0 && !branchCode.isEmpty())
		{
			//This dummy balance calculation should be altered later by actual business code
			retValue+=fakeLoanInfo();
		}
		else {
			retValue+= "N/A";
		}

		//final processing on response string
		return retValue;
	}
	/**
	 * @param min
	 * @param max
	 * @return a random number between min and max
	 */
	private Double getRandomBalance(Double min, Double max) {

		double randomNum =0.00;
		Random rand;
		try {
			rand = Random.class.newInstance();
			randomNum = min + (max - min) * rand.nextDouble();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return randomNum;
	}
	private Date getRandomDate()
	{
		GregorianCalendar gc = new GregorianCalendar();
		int year = randBetween(2016, 2020);
		gc.set(gc.YEAR, year);
		int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
		gc.set(gc.DAY_OF_YEAR, dayOfYear);
		return gc.getTime();
	}
	private int randBetween(int start, int end) {
		return start + (int)Math.round(Math.random() * (end - start));
	}
	private String fakeLoanInfo()
	{
		int numberOfLoans= new Random().nextInt(5);
		String infoString="";
		for(int loanCount=1;loanCount<=numberOfLoans;loanCount++)
		{
			Double loan=getRandomBalance(10000.00, 150000.00);
			Double paid=getRandomBalance(1000.00, 10000.00);
			infoString+="LOAN_NUMBER:"+loanCount+", LOAN_AMT:"+df.format(loan)+", PAID_AMT:"+df.format(paid)+",REMAINING_AMT:"+df.format((loan-paid))+"  ";
		}
		return infoString;		
	}
	private DecimalFormat df = new DecimalFormat("##,##0.00");
}
