package com.servlet.micropostfull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmm.Ngram;
import tmm.Topic;
import tmm.data.DataLoader;
import tmm.lda.TopicDetector;

import com.google.gson.Gson;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;


/**
 * Servlet implementation class Main
 */
@WebServlet("/Main")
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static String str;
	static String dataDirectory,tweetsFile;
	//private static final String TWITTER_DATE_FORMAT = "EEE MMM dd HH:mm:ss Z yyyy";
	private static final String TWITTER_DATE_FORMAT ="MMM dd, yyyy HH:mm:ss a";

	static Gson gson = new Gson();

    /**
     * Default constructor. 
     */
    public Main() {
        // TODO Auto-generated constructor stub
    	System.out.println(new File(".").getAbsolutePath());
    	System.out.println(System.getProperty("java.classpath"));
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}	
	
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String result="";
		
        String dat = request.getParameter("date");
        String time1 = request.getParameter("date1");
        String time2 = request.getParameter("date2");
        //System.out.println("\nType 1(LDA), 2(Doc-p), 3(GraphBased), 4(sfim), 5(BNgram)");
        String menu = request.getParameter("menu");
        String keyword = request.getParameter("keyword");
        
        System.out.println(keyword+""+dat+"  "+time1+"   "+time2+"  "+menu);
        
        /***** Twitter Api  ****/
        
        ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("bJlsALLd7Gvaaeqp2a0ay5QZC")
		  .setOAuthConsumerSecret("vvBmATRDExtO9SjCHz56r21pEyNdJwgVVkcizVCe0JOdrMMQ4d")
		  .setOAuthAccessToken("925353571-frp0XXINItS3Udl1YxMjWsczgjXoX6tUOeIcqzzB")
		  .setOAuthAccessTokenSecret("Ur3RD1EaACazKLgtVmF48hSafQIwkcImtBm9qNBsBirDw");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		  final String OLD_FORMAT = "dd/MM/yyyy";
           final String NEW_FORMAT = "yyyy-MM-dd";
           String newDate;
           SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
           Date d=null;
		try {
			d = sdf.parse(dat);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
           sdf.applyPattern(NEW_FORMAT);
           newDate = sdf.format(d); 
	    Query query =(new  Query(keyword)).until(newDate);
	    QueryResult queryResult;
	    try{
	    new File("data.json").delete();
	    }catch(Exception e){}
	    FileWriter fileWriten = new FileWriter(new File("data.json"));
		BufferedWriter bufferWrn = new BufferedWriter(fileWriten);
		try {
			queryResult = twitter.search(query);
			for (Status status : queryResult.getTweets()) {
				bufferWrn.write(gson.toJson(status)+"\n");
		    }
			bufferWrn.close();	
			//System.out.println(result.getTweets());
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
        /********************************/
        try
		{
        String inputstream;  
        inputstream = "data.json";
        BufferedReader bufferRd = new BufferedReader(new FileReader(inputstream)); 
	    SimpleDateFormat dformat = new SimpleDateFormat("HH:mm");
	    Date d1 = dformat.parse(time1);
		Date d2 = dformat.parse(time2);

		//in milliseconds
		long diff = d2.getTime() - d1.getTime();
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(d1);
		 cal.add(Calendar.MILLISECOND, (int) -diff);
		 String prevTime = dformat.format(cal.getTime());
		 System.out.print(prevTime);
		 
	    //////////* Create tweet file for timeslot */////////////
	    String filename = dat.substring(0,2)+"_"+dat.substring(3,5)+"_"+dat.substring(6)+"_"+time1.substring(0,2)+"_"+time1.substring(3)+".json";
		FileWriter fileWrite = new FileWriter(new File(filename));
		BufferedWriter bufferWr = new BufferedWriter(fileWrite);
		  
		while((str = bufferRd.readLine()) != null) 
		{	
			  
              Tweet p = gson.fromJson(str, Tweet.class);
            if (!(p==null)){
            	 
                SimpleDateFormat df = new SimpleDateFormat(TWITTER_DATE_FORMAT, Locale.ENGLISH);
                if (!(p.getCreated_at()==null)){
            	Date twiDate = df.parse(p.getCreated_at());
            	SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            	Date date1 = df1.parse(dat+" "+time1);
            	Date date2 = df1.parse(dat+" "+time2);
            if (twiDate.after(date1) && twiDate.before(date2))
            {
            	bufferWr.write(str+"\n");
            }} }
		}
		bufferWr.close();
		bufferRd.close();
		
		 //////////* Create tweet file for previous timeslot */////////////
	    String filenamep = dat.substring(0,2)+"_"+dat.substring(3,5)+"_"+dat.substring(6)+"_"+prevTime.substring(0,2)+"_"+prevTime.substring(3)+".json";
		FileWriter fileWritep = new FileWriter(new File(filenamep));
		BufferedWriter bufferWrp = new BufferedWriter(fileWritep);
		BufferedReader bufferRdp = new BufferedReader(new FileReader(inputstream)); 
		
		while((str = bufferRdp.readLine()) != null) 
		{	
              Tweet p = gson.fromJson(str, Tweet.class);
            if (!(p==null)){
            	 
                SimpleDateFormat df = new SimpleDateFormat(TWITTER_DATE_FORMAT, Locale.ENGLISH);
                if (!(p.getCreated_at()==null)){
            	Date twiDate = df.parse(p.getCreated_at());
            	SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            	Date date1 = df1.parse(dat+" "+prevTime);
            	Date date2 = df1.parse(dat+" "+time1);
            	
            if (twiDate.after(date1) && twiDate.before(date2))
            {
            	bufferWrp.write(str+"\n");
            }} }
		}
		bufferWrp.close();
		bufferRdp.close();
		 
		   /////////////////////////////************** Menu *************//////////////////////////////
	    String choice= menu;
	    List<Topic> topics = null;
	    //String workingDir = System.getProperty("user.dir");
	    List<tmm.Tweet> tweets=DataLoader.loadData(filename);
	            
	        if ("1".equals(choice)) {
				//create the topic detector
				TopicDetector topicDetectorLDA = new TopicDetector();
				//get a list of topics by calling createTopics.
			     topics = topicDetectorLDA.createTopics(tweets);
				 // Printing topics
				  for(Topic t:topics) {
					  System.out.println("\t");
					  for(Ngram n:t.getKeywords()){
						  result += n.getTerm().toString().replaceAll(":", " ");
					  }
					  result+="<br>";  
			        }
	        }
	        if ("2".equals(choice)) {
	        	TopicDetector topicDetectorDocPivot = new TopicDetector();
	        	topics = topicDetectorDocPivot.createTopics(tweets);
	        	// Printing topics
				  for(Topic t:topics) {
					  System.out.println("\t");
					  for(Ngram n:t.getKeywords())
						  result = n.getTerm().toString().replaceAll(":", " ");
				          result += "<br>";
			        }
	        }
	        if ("3".equals(choice)) {
	        	tmm.graphbased.TopicDetector topicDetectorGraph = new tmm.graphbased.TopicDetector();
	        	topics = topicDetectorGraph.createTopics(tweets);
	        	// Printing topics
				  for(Topic t:topics) {
					  System.out.println("\t");
					  for(Ngram n:t.getKeywords())
						  result = n.getTerm().toString().replaceAll(":", " ");
				          result += "<br>";
			        }
	        }
	        if ("4".equals(choice)) {
	        	TopicDetector topicDetectorSFIM = new TopicDetector();
		        topics = topicDetectorSFIM.createTopics(tweets);
	        	// Printing topics
				  for(Topic t:topics) {
					  System.out.println("\t");
					  for(Ngram n:t.getKeywords())
						  result = n.getTerm().toString().replaceAll(":", " ");
				          result += "<br>";
			        }
	        }
	        if ("5".equals(choice)) {
	        	tmm.bngram.TopicDetector topicDetectorBNgram = new tmm.bngram.TopicDetector("",filenamep);
	        	topics = topicDetectorBNgram.createTopics(tweets);
	        	// Printing topics
				  for(Topic t:topics) {
					  System.out.println("\t");
					  for(Ngram n:t.getKeywords())
						  result = n.getTerm().toString().replaceAll(":", " ");
				          result += "<br>";
			        }
	        }
	        try{
	        new File(filename).delete();
	        new File(filenamep).delete();
	        }catch(Exception e){}
	    /////////////////////////******************//////////////////////////////////
	

} catch (Exception e)
{
	e.printStackTrace();
}

        /**********************************/
       
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /*  output   */

            out.println(result);

        } finally {
            out.close();
        }
	}

}
