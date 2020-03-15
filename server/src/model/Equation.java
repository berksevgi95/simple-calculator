package model;

public class Equation {
	
	private Integer number1;
	private String operation;
	private Integer number2;
	private String message;
	
	public Equation(Integer number1, String operation, Integer number2, String message) {
		this.number1 = number1;
		this.operation = operation;
		this.number2 = number2;
		this.message = message;
	}
	
	public String execute() {
		switch(this.operation) {
		case "*":
			return this.message + (this.number1 * this.number2);
		case "+":
			return this.message + (this.number1 + this.number2);
		case "-":
			return this.message + (this.number1 - this.number2);
		case "/":
			return this.message + (this.number1 / this.number2);
		default:
			return "Undefined operation";
		}
	}

	public Integer getNumber1() {
		return number1;
	}

	public void setNumber1(Integer number1) {
		this.number1 = number1;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Integer getNumber2() {
		return number2;
	}

	public void setNumber2(Integer number2) {
		this.number2 = number2;
	}
	
}