package Extractor;

public class DateRange {
	
	private String startDate; // store in string form. when it needs to be compare, compare using SimpleDateFormat 
	private String endDate;
	
	public DateRange() {
		// TODO Auto-generated constructor stub
	}

	public DateRange(String startDateOfWeek, String endDateOfWeek) {
		this.startDate = startDateOfWeek;
		this.endDate = endDateOfWeek;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
