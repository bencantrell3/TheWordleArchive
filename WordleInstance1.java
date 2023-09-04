
public class WordleInstance1 {
	String word;
    int num;
    int monthNum;
    String month;
    int day;
    int year;
    String[] monthArray = {"January","February","March","April","May","June","July","August","September","October","November","December"};

    public WordleInstance1(String rawInput) {
    	  rawInput = rawInput.substring(0, rawInput.indexOf('<'));
    	  if(rawInput.indexOf('a') >= 0) {
    		  rawInput = rawInput.substring(0,rawInput.indexOf('a')) + rawInput.substring(rawInput.indexOf('a')+1);
    	  }
    	  else if(rawInput.indexOf('b') >= 0) {
    		  return;
    	  }
          word = rawInput.substring(0,6);
          int secondSpace = (rawInput.substring(rawInput.indexOf('#'),rawInput.length())).indexOf(' ') + rawInput.indexOf('#');//giving index of substring instead of string
          num = Integer.valueOf(rawInput.substring(rawInput.indexOf('#')+1,secondSpace));
          monthNum = Integer.valueOf(rawInput.substring(rawInput.indexOf('/')-2,rawInput.indexOf('/')));      
          month = monthArray[monthNum-1];
          day = Integer.valueOf(rawInput.substring(rawInput.indexOf('/')+1,rawInput.indexOf('/')+3));
          year = Integer.valueOf("20" + rawInput.substring(rawInput.indexOf('/')+4,rawInput.length()));

    }
    
    public WordleInstance1(String rawInput,String trash) {
    	word = rawInput.substring(16,21);
    	rawInput = rawInput.substring(0,rawInput.indexOf(" ")) + rawInput.substring(rawInput.indexOf(" ")+1,rawInput.length());
    	num = Integer.valueOf(rawInput.substring(11,rawInput.indexOf(" ")));
    	monthNum = Integer.valueOf(rawInput.substring(5,7));
    	month = monthArray[monthNum-1];
    	day = Integer.valueOf(rawInput.substring(8,10));
    	year = Integer.valueOf(rawInput.substring(0,4));
    	/*
    	System.out.println("Word : " + word);
    	System.out.println("num : " + num);
    	System.out.println("monthnum : " + monthNum);
    	System.out.println("month : " + month);
    	System.out.println("day : " + day);
    	System.out.println("year : " + year);
    	*/
    }


/*
    public static void main(String[] args) {
          String text = "CANNY #323 05/08/22<";
          WordleInstance w = new WordleInstance(text);
          System.out.println(w.word);
          System.out.println(w.num);
          System.out.println(w.month);
          System.out.println(w.day);
          System.out.println(w.year);
    }
    */
}