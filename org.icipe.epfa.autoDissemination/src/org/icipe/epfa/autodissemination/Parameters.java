package org.icipe.epfa.autodissemination;

public class Parameters {
	private String strStdPar1="",strStdPar2="",strStdPar3="",strStdPar4="",strStdPar5="",strStdPar6="",strStdPar7="";
	private String strPar1="",strPar2="",strPar3="",strPar4="",strPar5="",strPar6="",strPar7="";
	private String strNamePar1="",strNamePar2="",strNamePar3="",strNamePar4="",strNamePar5="",strNamePar6="",strNamePar7="";
	private String strModel="";
	
	public void setParameters(String strPar1,String strPar2,String strPar3,String strPar4,String strPar5,String strPar6,String strPar7){
		this.strPar1 = strPar1;
		this.strPar2 = strPar2;
		this.strPar3 = strPar3;
		this.strPar4 = strPar4;
		this.strPar5 = strPar5;
		this.strPar6 = strPar6;
		this.strPar7 = strPar7;
	}
	
	public String[] getParameters(){
		return new String[]{strPar1,strPar2,strPar3,strPar4,strPar5,strPar6,strPar7};
	}
	
	public String[] getStdParameters(){
		return new String[]{strStdPar1,strStdPar2,strStdPar3,strStdPar4,strStdPar5,strStdPar6,strStdPar7};
	}
	
	public String[] getParametersName(){
		return new String[]{strNamePar1,strNamePar2,strNamePar3,strNamePar4,strNamePar5,strNamePar6,strNamePar7};
	}
	
	public void setParameters(Double[] strPars){
		this.strPar1 = Double.valueOf(strPars[0]).toString();
		this.strPar2 = Double.valueOf(strPars[1]).toString();
		this.strPar3 = Double.valueOf(strPars[2]).toString();
		this.strPar4 = Double.valueOf(strPars[3]).toString();
		this.strPar5 = Double.valueOf(strPars[4]).toString();
		this.strPar6 = Double.valueOf(strPars[5]).toString();
		this.strPar7 = Double.valueOf(strPars[6]).toString();
	}
	
	public void setParametersName(String[] strNamePars){
		this.strNamePar1 = strNamePars[0];
		this.strNamePar2 = strNamePars[1];
		this.strNamePar3 = strNamePars[2];
		this.strNamePar4 = strNamePars[3];
		this.strNamePar5 = strNamePars[4];
		this.strNamePar6 = strNamePars[5];
		this.strNamePar7 = strNamePars[6];
	}

	public void setStdParameters(Double[] strPars){
		this.strStdPar1 = Double.valueOf(strPars[0]).toString();
		this.strStdPar2 = Double.valueOf(strPars[1]).toString();
		this.strStdPar3 = Double.valueOf(strPars[2]).toString();
		this.strStdPar4 = Double.valueOf(strPars[3]).toString();
		this.strStdPar5 = Double.valueOf(strPars[4]).toString();
		this.strStdPar6 = Double.valueOf(strPars[5]).toString();
		this.strStdPar7 = Double.valueOf(strPars[6]).toString();
	}
	
	public String getStrModel() {
		return strModel;
	}

	public void setStrModel(String strModel) {
		this.strModel = strModel;
	}

	
}
