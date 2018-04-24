#install.packages("minpack.lm", dependencies = T) #for the function nls.lm()
library(minpack.lm)
#install.packages("MASS", dependencies = T) # for the function ginv()
library(MASS)
formatMortalityData <- function (dirToFile)
{
#################################################################################################################
#xRange,yRange,xLabel, yLabel,graphTitle

		#This function take as input a file of mortality data and plot them after some basic transformation.
		#It return as output the transformed data will use to fitting model
		#dataFile : the file containing mortality data to be fitted.  plot
		#xRange,yRange : interval of value for X and Y abscissa. e.g xRange = c(0,45),yRange = c(0,100)
		#xLabel, yLabel,graphTitle: respectively, X, Y and graph title.
	
#################################################################################################################	
#dirToFile = paste("./Data/",dataFile,sep="")
	dat = read.table(dirToFile, h = T) # reading of data in the file
	
	#assignment of names corresponding to column number of the loaded file.
	Temp=1
	Rep=2
	Survi=4
	Mort=5
	
	#Transformation of data for display
	freqm=aggregate(dat[,Mort],list(Temperature=dat[,Temp]),sum)  # we sum matality by their temperature
	max1=aggregate(dat[,Survi],list(Temperature=dat[,Temp]),max)
	n1=aggregate(dat[,Mort],list(Temperature=dat[,Temp],Replicate=dat[,Rep]),length)
	n2=data.frame(table(n1[,1]))
  
	tablaM=data.frame(freqm,N=n2[,2]*max1[,2],Mort=freqm[,2]/(n2[,2]*max1[,2]))
	y=round(tablaM[,4],4)
	x=tablaM[,1]
  
  datm=data.frame(x,y)
  
  #Ploting of data
  
	#	plot(datm[,1],datm[,2]*100,frame=F,pch=19,col=4,cex=1.3,xlim=xRange,ylim=yRange,axes=F,xaxt = "n",xlab=xLabel,ylab=yLabel,main=graphTitle)
	
	cat("MORTALITY FOR TEMPERATURE\n")
	dat <- data.frame(T = datm[,1], Mortality = datm[,2])
		
	#axis(1, xaxp=c(xRange,5))
	#axis(2,las=2);
	print(dat)   # Displays of transformed data.
	return(datm);
}
##This function is use to generate initial valur for function when initiate parameter
pr6mortal<-function(modelm,datm)
{
	x<-datm[,1]
	y<-datm[,2]
	if(modelm==1)  modelo<-lm(y~x+I(x^2))
	if(modelm==2)  modelo<-lm(y~x+I(sqrt(x)))
	if(modelm==3)  modelo<-lm(y~x+I(1/sqrt(x)))
	if(modelm==4)  modelo<-lm(y~x+I(1/x^2))
	if(modelm==5)  modelo<-lm(y~x+I(1/x))
	if(modelm==6)  modelo<-lm(y~x+I(log(x)))
	coefi<-round(as.numeric(coef(modelo)),3)
	for(i in 1:length(coefi))if(coefi[i]==0) coefi[i]<-0.001
	coefi<-c("a"=coefi[3],"b"=coefi[2],"c"=coefi[1])
	for (i in names(coefi))
	{
		temp <- coefi[[i]]
		storage.mode(temp) <- "double"
		assign(i, temp)
	}
	salidas<-list(ini=coefi)
	return(salidas)
}


#use to plot the curve after estimate the parameter
pruebamortal<-function(modelNumber,datm,inim,corrx,corry,mini,maxi,labx, laby,titulo)
{
	if(modelNumber==1)  form<-expression(a*xlinea^2+b*xlinea+c)
	if(modelNumber==2)  form<-expression(a*sqrt(xlinea)+b*xlinea+c)
	if(modelNumber==3)  form<-expression(a*(1/sqrt(xlinea))+b*xlinea+c)
	if(modelNumber==4)  form<-expression(a*(1/xlinea^2)+b*xlinea+c)
	if(modelNumber==5)  form<-expression(a*(1/xlinea)+b*xlinea+c)
	if(modelNumber==6)  form<-expression(b*xlinea+a*log(xlinea)+c)
	if(modelNumber==7)  form<-expression(1/(1+a*exp(-b*((xlinea-c)/d)^2))) # log normal, 4 parameters
	if(modelNumber==8) form<-expression((a*exp(-b*((xlinea-xo)/c)^2)))
	if(modelNumber==9)  form<-expression(y0 + a * exp(-0.5 * ((xlinea-x0)/b)^2))
	if(modelNumber==10) form<-expression(y0 + a * exp(-0.5 * (log(abs(xlinea/x0))/b)^2))

	# nuevos modelos
	if(modelNumber==11) form<-expression(b1+b2*xlinea+b3*xlinea^d)
	if(modelNumber==12) form<-expression(exp(b1+b2*xlinea+b3*xlinea^2))
	if(modelNumber==13) form<-expression(1-(b4/(1+b5*exp(b1+b2*xlinea+b3*xlinea^2))))
	if(modelNumber==14) form<-expression(exp(b1+b2*xlinea+b3*sqrt(xlinea)))
	if(modelNumber==15) form<-expression(1-(b4/(1+b5*exp(b1+b2*xlinea+b3*sqrt(xlinea)))))
	if(modelNumber==16) form<-expression(exp(b1+b2*xlinea+b3*(1/sqrt(xlinea))))
	if(modelNumber==17) form<-expression(1-(b4/(1+b5*exp(b1+b2*xlinea+b3*(1/sqrt(xlinea))))))
	if(modelNumber==18) form<-expression(exp(b1+b2*xlinea+b3*(1/xlinea)))
	if(modelNumber==19) form<-expression(1-(b4/(1+b5*exp(b1+b2*xlinea+b3*(1/xlinea)))))
	if(modelNumber==20) form<-expression(exp(b1+b2*xlinea+b3*xlinea^d))
	if(modelNumber==21) form<-expression(1-(b4/(1+b5*exp(b1+b2*xlinea+b3*xlinea^d))))
	if(modelNumber==22) form<-expression(exp(b1+b2*xlinea+b3*log(xlinea)))
	if(modelNumber==23) form<-expression(1-(b4/(1+b5*exp(b1+b2*xlinea+b3*log(xlinea)))))
	if(modelNumber==24) form<-expression(1-rm*exp((-0.5)*(-(xlinea-Topt)/Troh)^2))
	if(modelNumber==25) form<-expression(1-rm*exp((-0.5)*(-(log(xlinea)-log(Topt))/Troh)^2))
	if(modelNumber==26) form<-expression(1 - 1/(exp((1+exp(-(xlinea-Topt)/B))*(1+exp(-(Topt-xlinea)/B))*H)))
	if(modelNumber==27) form<-expression(1 - 1/(exp((1+exp(-(xlinea-Tl)/B))*(1+exp(-(Th-xlinea)/B))*H)))
	if(modelNumber==28) form<-expression(1 - 1/(exp((1+exp(-(xlinea-Topt)/Bl))*(1+exp(-(Topt-xlinea)/Bh))*H)))
	if(modelNumber==29) form<-expression(1 - 1/(exp((1+exp(-(xlinea-Tl)/Bl))*(1+exp(-(Th-xlinea)/Bh))*H)))
	if(modelNumber==30) form<-expression(1 - H/(exp(1+exp(-(xlinea-Topt)/B))*(1+exp(-(Topt-xlinea)/B))))
	if(modelNumber==31) form<-expression(1 - H/(exp(1+exp(-(xlinea-Tl)/B))*(1+exp(-(Th-xlinea)/B))))
	if(modelNumber==32) form<-expression(1 - H/(exp(1+exp(-(xlinea-Topt)/Bl))*(1+exp(-(Topt-xlinea)/Bh))))
	if(modelNumber==33) form<-expression(1 - H/(exp(1+exp(-(xlinea-Tl)/Bl))*(1+exp(-(Th-xlinea)/Bh))))
	if(modelNumber==34) form<-expression(Bm + ((1-1/(1+exp((Hl/1.987)*((1/Tl)-(1/(xlinea+273.15))))+exp((Hh/1.987)*((1/Th)-(1/(xlinea+273.15))))))*(1-Bm))) #DM: se agrego 2 parentesis "Bm + (...)*"
	if(modelNumber==35) form<-expression((1 - exp(-(exp(a1+b1*xlinea)))) + (1 - exp(-(exp(a2+b2*xlinea)))))
	if(modelNumber==36) form<-expression((w-xlinea)^(-1))
	if(modelNumber==37) form<-expression(a1*exp(b1*xlinea) + a2*exp(b2*xlinea))
	if(modelNumber==38) form<-expression(a1*exp(b1*xlinea) + a2*exp(b2*xlinea)+c1)
	if(modelNumber==39) form<-expression(a*(abs(xlinea-b))^nn)
	if(modelNumber==40) form<-expression(a*xlinea*(xlinea-To)*(Tl-xlinea)^(1/d))
	if(modelNumber==41) form<-expression(exp(a*xlinea*(xlinea-To)*(Tl-xlinea)^(1/d)))
	if(modelNumber==42) form<-expression(a*((xlinea-Tmin)^n)*(Tmax-xlinea)^m)
	if(modelNumber==43) form<-expression(1/((Dmin/2) * (exp(k*(xlinea-Tp)) + exp(-(xlinea-Tp)*lamb))))
	if(modelNumber==44) form<-expression(a*(1-exp(-(xlinea-Tl)/B))*(1-exp(-(Th-xlinea)/B)))
	if(modelNumber==45) form<-expression(exp(a*(1-exp(-(xlinea-Tl)/Bl))*(1-exp(-(Th-xlinea)/Bh))))

	# fin de nuevos modelos

	plot(datm[,1],datm[,2]*100,frame=F,pch=19,col=4,cex=1.3,xlim=corrx,ylim=corry,axes=F,xaxt = "n",xlab=labx,ylab=laby,main=titulo)
	
	axis(1, xaxp=c(corrx,5))
	axis(2,las=2)
	ini<-as.list(inim)
	for (i in names(ini))
	{
		temp <- ini[[i]]
		storage.mode(temp) <- "double"
		assign(i, temp)
	}
	xlinea<-seq(mini,maxi,length=100)
	ylinea<-eval(form)
	lines(xlinea,ylinea*100,lwd=2,col=2)
	
	salidas<-list(ini=as.data.frame(ini))
	return(salidas)
}





dead_func <- function (modelNumber, datm, alg, inim, weight,weights)
{
###############################################################################################################################
	#This function take as input the transformed version of mortality data(datm), a number refering to a model(modelNumber)
	# and the name of an algorithm(alg) of fitting data will be used to fit the model to data. and give as output
	#the expression of the choosen model with his parameter estimated.
	#weight: 
	# modelNumber(number regering to modle to be fitted), datm(transformed  mortality data), alg(algorithm to be used to estimate parameter), inim(initial parameter)
	
###############################################################################################################################

  x<-datm[,1] 
  y<-datm[,2]
  if(alg=="Newton") #in case on gauss-Newtown algorithm (GNA)
  {
	
    if(modelNumber==1)
    {
      form<-y~a*x^2+b*x+c
      frm<- "m(T) = aT²+bT+c" 
    }
    if(modelNumber==2 )
    {
      form<-y~a*sqrt(x)+b*x+c
      frm<- "m(T) = a·sqrt(T)+bT+c" 
    }
    if(modelNumber==3)
    {
      form<-y~1/(1+a*exp(-b*((x-c)/d)^2))
       frm<- "m(T) = a/sqrt(T)+bT+c" 
    }
	 if(modelNumber==7)
    {
		form<-y~a*(1/sqrt(x))+b*x+c
       frm<- "m(T) = 1/(1+a·exp(-b((x-c)/d)²))" 
      
    }
	if(weight=="WLS") model<-nls(form,inim,data=datm,weights=weights)#function use for parameter estimation using Newton method and weight.
		if(weight=="LS") model<-nls(form,inim,data=datm)
		coefi<-coef(model)
		return(list(estimatedCoef=coefi,f=form,model=model,ecua=frm))
  }
  
  if(alg=="Marquardtr")  # in case of Levenberg-Marquardtr algorithm (MLE)
  {
    ini<-as.list(inim)
    for (i in names(ini)) #to insure to the type of variable is double
	{
      temp <- ini[[i]]
      storage.mode(temp) <- "double"
      assign(i, temp)
    }
	 if(modelNumber==1 )
    {
      
	  f <- function(x,a,b,c)
      {
        expr <- expression(a*x^2+b*x+c)
        eval(expr)
      }
      j <- function(x,a,b,c)  #function computing the Jacobian
      {
        expr <- expression(a*x^2+b*x+c)
        c(eval(D(expr, "a")),eval(D(expr, "b")), eval(D(expr, "c")))
      }
      g<-y~a*x^2+b*x+c
       frm<- "m(T) = aT²+bT+c"
    }
	
	if(modelNumber==2 )
    {
      
	  f <- function(x,a,b,c)
      {
        expr <- expression(a*sqrt(x)+b*x+c)
        eval(expr)
      }
      j <- function(x,a,b,c)
      {
        expr <- expression(a*sqrt(x)+b*x+c)
        c(eval(D(expr, "a")),eval(D(expr, "b")), eval(D(expr, "c")))
      }
      g<-y~a*x^2+b*x+c
       frm<- "m(T) = a sqrt(T)+bT+c"
    }
	if(modelNumber==3)
	{			
			
		f <- function(x,a,b,c)
		  {
			expr <- expression(a*(1/sqrt(x))+b*x+c)
			eval(expr)
		  }
		  j <- function(x,a,b,c)
		  {
			expr <- expression(a*(1/sqrt(x))+b*x+c)
			c(eval(D(expr, "a")),eval(D(expr, "b")), eval(D(expr, "c")))
	  }
	  g<-y~a*(1/sqrt(x))+b*x+c
	   frm<- "m(T) = a/sqrt(T)+bT+c"
	}	

	if(modelNumber==4)
		{
						
			f <- function(x,a,b,c)
			  {
				expr <- expression(a*(1/x^2)+b*x+c)
				eval(expr)
			  }
			  j <- function(x,a,b,c)
			  {
				expr <- expression(a*(1/x^2)+b*x+c)
				c(eval(D(expr, "a")),eval(D(expr, "b")), eval(D(expr, "c")))
			  }
			  g<-y~a*(1/x^2)+b*x+c
			   frm<- "m(T) = a/T²+bT+c"
		}
		
		
	if(modelNumber==5)
		{
						
			f <- function(x,a,b,c)
			  {
				expr <- expression(a*(1/x)+b*x+c)
				eval(expr)
			  }
			  j <- function(x,a,b,c)
			  {
				expr <- expression(a*(1/x)+b*x+c)
				c(eval(D(expr, "a")),eval(D(expr, "b")), eval(D(expr, "c")))
			  }
			  g<-y~a*(1/x)+b*x+c
			   frm<- "m(T) = a/T+bT+c"
		}

	
	if(modelNumber==6)
		{
						
			f <- function(x,a,b,c)
			  {
				expr <- expression(b*x+a*log(x)+c)
				eval(expr)
			  }
			  j <- function(x,a,b,c)
			  {
				expr <- expression(b*x+a*log(x)+c)
				c(eval(D(expr, "a")),eval(D(expr, "b")), eval(D(expr, "c")))
			  }
			  g<-y~b*x+a*log(x)+c
			   frm<- "m(T) = a·ln(T)+bT+c"
		}
	
	if(modelNumber==7)
    {
      f <- function(x,a,b,c,d)
      {
        expr <- expression(1/(1+a*exp(-b*((x-c)/d)^2)))
        eval(expr)
      }
      j <- function(x,a,b,c,d)
      {
        expr <- expression(1/(1+a*exp(-b*((x-c)/d)^2)))
        c(eval(D(expr, "a")),eval(D(expr, "b")), eval(D(expr, "c")), eval(D(expr, "d")))
      }
      g<-y~1/(1+a*exp(-b*((x-c)/d)^2))
      frm<- "m(T) = 1/(1+a·exp(-b((x-c)/d)²))" 
    }
	
	if(modelNumber==8)
		{
			f <- function(x,a,b,c,xo)
			{
				expr <- expression((a*exp(-b*((x-xo)/c)^2)))
				eval(expr)
			}
			j <- function(x,a,b,c,xo)
			{
				expr <- expression((a*exp(-b*((x-xo)/c)^2)))
				c(eval(D(expr, "a")),eval(D(expr, "b")), eval(D(expr, "c")), eval(D(expr, "xo")))
			}
			g<-y~a*exp(-b*((x-xo)/c)^2)
			frm<- "m(T) = a·exp(-b((x-xo)/c)²)" 
		}
    
    if(modelNumber==9)
    {
      f <- function(x,a,b,y0,x0)
      {
        expr <- expression(y0 + a * exp(-0.5 * ((x-x0)/b)^2))
        eval(expr)
      }
      j <- function(x,a,b,y0,x0)
      {
        expr <- expression(y0 + a * exp(-0.5 * ((x-x0)/b)^2))
        c(eval(D(expr, "a")),eval(D(expr, "b")), eval(D(expr, "x0")), eval(D(expr, "y0")))
      }
      g<-y~y0 + a * exp(-0.5 * ((x-x0)/b)^2)
       frm<- "m(T) = y0+a·exp(-0.5((x-x0)/b)²)" 
    }
    
		if(modelNumber==10){
			f <- function(x,y0,a,b,x0)
			{
				expr <- expression(y0 + a * exp(-0.5 * (log(abs(x/x0))/b)^2))
				eval(expr)
			}
			j <- function(x,y0,a,b,x0)
			{
				expr <- expression(y0 + a * exp(-0.5 * (log(abs(x/x0))/b)^2))
				c(eval(D(expr, "y0")),eval(D(expr, "a")), eval(D(expr, "b")), eval(D(expr, "x0")))
			}
			g<-y~y0 + a * exp(-0.5 * (log(abs(x/x0))/b)^2)
			frm<- "m(T) = y0+a·exp(-0.5(log(abs(x/x0))/b)²)"
		}
		
		if(modelNumber==11){
			f <- function(x,b1,b2,b3,d)
			{
				expr <- expression(b1+b2*x+b3*x^d)
				eval(expr)
			}
			j <- function(x,b1,b2,b3,d)
			{
				expr <- expression(b1+b2*x+b3*x^d)
				c(eval(D(expr, "b1")),eval(D(expr, "b2")), eval(D(expr, "b3")), eval(D(expr, "d")))
			}
			g<-y ~ b1+b2*x+b3*x^d
			 frm<- "m(T) = b1+b2.x+b3.x^d "
		}


		if(modelNumber==12){
			f <- function(x,b1,b2,b3)
			{
				expr <- expression(exp(b1+b2*x+b3*x^2))
				eval(expr)
			}
			j <- function(x,b1,b2,b3)
			{
				expr <- expression(exp(b1+b2*x+b3*x^2))
				c(eval(D(expr, "b1")),eval(D(expr, "b2")), eval(D(expr, "b3")))
			}
			g<-y ~ exp(b1+b2*x+b3*x^2)
			frm<- "m(T) = exp(b1+b2.x+b3.x²) "
		}

		if(modelNumber==13){
			f <- function(x,b1,b2,b3,b4,b5)
			{
				expr <- expression(1-(b4/(1+b5*exp(b1+b2*x+b3*x^2))))
				eval(expr)
			}
			j <- function(x,b1,b2,b3,b4,b5)
			{
				expr <- expression(1-(b4/(1+b5*exp(b1+b2*x+b3*x^2))))
				c(eval(D(expr, "b1")),eval(D(expr, "b2")), eval(D(expr, "b3")), eval(D(expr, "b4")), eval(D(expr, "b5")))
			}
			g<-y ~ 1-(b4/(1+b5*exp(b1+b2*x+b3*x^2)))
			frm<- "m(T) = 1-(b4/(1+b5.exp(b1+b2.x+b3.x²)))"
		}

		if(modelNumber==14){
			f <- function(x,b1,b2,b3)
			{
				expr <- expression(exp(b1+b2*x+b3*sqrt(x)))
				eval(expr)
			}
			j <- function(x,b1,b2,b3)
			{
				expr <- expression(exp(b1+b2*x+b3*sqrt(x)))
				c(eval(D(expr, "b1")),eval(D(expr, "b2")), eval(D(expr, "b3")))
			}
			g<-y ~ exp(b1+b2*x+b3*sqrt(x))
			frm<- "m(T) = exp(b1+b2.x+b3.(x)^0.5)" 
		}



		if(modelNumber==15){
			f <- function(x,b1,b2,b3,b4,b5)
			{
				expr <- expression(1-(b4/(1+b5*exp(b1+b2*x+b3*sqrt(x)))))
				eval(expr)
			}
			j <- function(x,b1,b2,b3,b4,b5)
			{
				expr <- expression(1-(b4/(1+b5*exp(b1+b2*x+b3*sqrt(x)))))
				c(eval(D(expr, "b1")),eval(D(expr, "b2")), eval(D(expr, "b3")), eval(D(expr, "b4")), eval(D(expr, "b5")))
			}
			g<-y ~ exp(1-(b4/(1+b5*exp(b1+b2*x+b3*sqrt(x)))))
			frm<- "m(T) = 1-(b4/(1+b5.exp(b1+b2.x+b3.(x)^0.5)))" 
		}


		if(modelNumber==16){
			f <- function(x,b1,b2,b3)
			{
				expr <- expression(exp(b1+b2*x+b3*(1/sqrt(x))))
				eval(expr)
			}
			j <- function(x,b1,b2,b3)
			{
				expr <- expression(exp(b1+b2*x+b3*(1/sqrt(x))))
				c(eval(D(expr, "b1")),eval(D(expr, "b2")), eval(D(expr, "b3")))
			}
			g<-y ~ exp(b1+b2*x+b3*(1/sqrt(x)))
			frm<- "m(T) = exp(b1+b2.x+b3.(1/(x)^0.5))" 
		}



		if(modelNumber==17){
			f <- function(x,b1,b2,b3,b4,b5)
			{
				expr <- expression(1-(b4/(1+b5*exp(b1+b2*x+b3*(1/sqrt(x))))))
				eval(expr)
			}
			j <- function(x,b1,b2,b3,b4,b5)
			{
				expr <- expression(1-(b4/(1+b5*exp(b1+b2*x+b3*(1/sqrt(x))))))
				c(eval(D(expr, "b1")),eval(D(expr, "b2")), eval(D(expr, "b3")), eval(D(expr, "b4")), eval(D(expr, "b5")))
			}
			g<-y ~ 1-(b4/(1+b5*exp(b1+b2*x+b3*(1/sqrt(x)))))
			frm<- "m(T) = 1-(b4/(1+b5.exp(b1+b2.x+b3.(1/(x)^0.5))))"
		}

		if(modelNumber==18){
			f <- function(x,b1,b2,b3)
			{
				expr <- expression(exp(b1+b2*x+b3*(1/x)))
				eval(expr)
			}
			j <- function(x,b1,b2,b3)
			{
				expr <- expression(exp(b1+b2*x+b3*(1/x)))
				c(eval(D(expr, "b1")),eval(D(expr, "b2")), eval(D(expr, "b3")))
			}
			g<-y ~ exp(b1+b2*x+b3*(1/x))
			frm<- "m(T) = exp(b1+b2.x+b3.(1/x))" 
		}



		if(modelNumber==19){
			f <- function(x,b1,b2,b3,b4,b5)
			{
				expr <- expression(1-(b4/(1+b5*exp(b1+b2*x+b3*(1/x)))))
				eval(expr)
			}
			j <- function(x,b1,b2,b3,b4,b5)
			{
				expr <- expression(1-(b4/(1+b5*exp(b1+b2*x+b3*(1/x)))))
				c(eval(D(expr, "b1")),eval(D(expr, "b2")), eval(D(expr, "b3")), eval(D(expr, "b4")), eval(D(expr, "b5")))
			}
			g<-y ~ 1-(b4/(1+b5*exp(b1+b2*x+b3*(1/x))))
			frm<- "m(T) = 1-(b4/(1+b5.exp(b1+b2.x+b3.(1/x))))"
		}


		if(modelNumber==20){
			f <- function(x,b1,b2,b3,d)
			{
				expr <- expression(exp(b1+b2*x+b3*x^d))
				eval(expr)
			}
			j <- function(x,b1,b2,b3,d)
			{
				expr <- expression(exp(b1+b2*x+b3*x^d))
				c(eval(D(expr, "b1")),eval(D(expr, "b2")), eval(D(expr, "b3")),eval(D(expr, "d")))
			}
			g<-y ~ exp(b1+b2*x+b3*x^d)
			frm<- "m(T) = exp(b1+b2.x+b3.x^d)"
		}

		if(modelNumber==21){
			f <- function(x,b1,b2,b3,b4,b5,d)
			{
				expr <- expression(1-(b4/(1+b5*exp(b1+b2*x+b3*x^d))))
				eval(expr)
			}
			j <- function(x,b1,b2,b3,b4,b5,d)
			{
				expr <- expression(1-(b4/(1+b5*exp(b1+b2*x+b3*x^d))))
				c(eval(D(expr, "b1")),eval(D(expr, "b2")), eval(D(expr, "b3")), eval(D(expr, "b4")), eval(D(expr, "b5")),eval(D(expr, "d")))
			}
			g<-y ~ 1-(b4/(1+b5*exp(b1+b2*x+b3*x^d)))
			frm<- "m(T) = 1-(b4/(1+b5.exp(b1+b2.x+b3.x^d)))"
		}


		if(modelNumber==22){
			f <- function(x,b1,b2,b3)
			{
				expr <- expression(exp(b1+b2*x+b3*log(x)))
				eval(expr)
			}
			j <- function(x,b1,b2,b3)
			{
				expr <- expression(exp(b1+b2*x+b3*log(x)))
				c(eval(D(expr, "b1")),eval(D(expr, "b2")), eval(D(expr, "b3")))
			}
			g<-y ~ exp(b1+b2*x+b3*log(x))
			frm<- "m(T) = exp(b1+b2.x+b3.ln(x))"
		}



		if(modelNumber==23){
			f <- function(x,b1,b2,b3,b4,b5)
			{
				expr <- expression(1-(b4/(1+b5*exp(b1+b2*x+b3*log(x)))))
				eval(expr)
			}
			j <- function(x,b1,b2,b3,b4,b5)
			{
				expr <- expression(1-(b4/(1+b5*exp(b1+b2*x+b3*log(x)))))
				c(eval(D(expr, "b1")),eval(D(expr, "b2")), eval(D(expr, "b3")), eval(D(expr, "b4")), eval(D(expr, "b5")))
			}
			g<-y ~ 1-(b4/(1+b5*exp(b1+b2*x+b3*log(x))))
			frm<- "m(T) = 1-(b4/(1+b5.exp(b1+b2.x+b3.ln(x))))" 
		}



		if(modelNumber==24){
			f <- function(x,rm,Topt,Troh)
			{
				expr <- expression(1-rm*exp((-0.5)*(-(x-Topt)/Troh)^2))
				eval(expr)
			}
			j <- function(x,rm,Topt,Troh)
			{
				expr <- expression(1-rm*exp((-0.5)*(-(x-Topt)/Troh)^2))
				c(eval(D(expr, "rm")),eval(D(expr, "Topt")), eval(D(expr, "Troh")))
			}
			g<-y ~ 1-rm*exp((-0.5)*(-(x-Topt)/Troh)^2)
			frm<- "m(T) = 1-rm.exp((-0.5).(-(x-Topt)/Troh)^2)" 
		}


		if(modelNumber==25){
			f <- function(x,rm,Topt,Troh)
			{
				expr <- expression(1-rm*exp((-0.5)*(-(log(x)-log(Topt))/Troh)^2))
				eval(expr)
			}
			j <- function(x,rm,Topt,Troh)
			{
				expr <- expression(1-rm*exp((-0.5)*(-(log(x)-log(Topt))/Troh)^2))
				c(eval(D(expr, "rm")),eval(D(expr, "Topt")), eval(D(expr, "Troh")))
			}
			g<-y ~ 1-rm*exp((-0.5)*(-(log(x)-log(Topt))/Troh)^2)
			frm<- "m(T) = 1-rm.exp((-0.5).(-(log(x)-log(Topt))/Troh)^2)"
		}


		if(modelNumber==26){
			f <- function(x,Topt,B,H)
			{
				expr <- expression(1 - 1/(exp((1+exp(-(x-Topt)/B))*(1+exp(-(Topt-x)/B))*H)))
				eval(expr)
			}
			j <- function(x,Topt,B,H)
			{
				expr <- expression(1 - 1/(exp((1+exp(-(x-Topt)/B))*(1+exp(-(Topt-x)/B))*H)))
				c(eval(D(expr, "Topt")),eval(D(expr, "B")), eval(D(expr, "H")))
			}
			g<-y ~ 1 - 1/(exp((1+exp(-(x-Topt)/B))*(1+exp(-(Topt-x)/B))*H))
			frm<- "m(T) = 1 - 1/(exp((1+exp(-(x-Topt)/B)).(1+exp(-(Topt-x)/B)).H))" 
		}


		if(modelNumber==27){
			f <- function(x,Tl,Th,B,H)
			{
				expr <- expression(1 - 1/(exp((1+exp(-(x-Tl)/B))*(1+exp(-(Th-x)/B))*H)))
				eval(expr)
			}
			j <- function(x,Tl,Th,B,H)
			{
				expr <- expression(1 - 1/(exp((1+exp(-(x-Tl)/B))*(1+exp(-(Th-x)/B))*H)))
				c(eval(D(expr, "Tl")),eval(D(expr, "Th")), eval(D(expr, "B")), eval(D(expr, "H")))
			}
			g<-y ~ 1 - 1/(exp((1+exp(-(x-Tl)/B))*(1+exp(-(Th-x)/B))*H))
			frm<- "m(T) = 1 - 1/(exp((1+exp(-(x-Tl)/B)).(1+exp(-(Th-x)/B)).H))"
		}


		if(modelNumber==28){
			f <- function(x,Topt,Bl,Bh,H)
			{
				expr <- expression(1 - 1/(exp((1+exp(-(x-Topt)/Bl))*(1+exp(-(Topt-x)/Bh))*H)))
				eval(expr)
			}
			j <- function(x,Topt,Bl,Bh,H)
			{
				expr <- expression(1 - 1/(exp((1+exp(-(x-Topt)/Bl))*(1+exp(-(Topt-x)/Bh))*H)))
				c(eval(D(expr, "Topt")),eval(D(expr, "Bl")),eval(D(expr, "Bh")), eval(D(expr, "H")))
			}
			g<-y ~ 1 - 1/(exp((1+exp(-(x-Topt)/Bl))*(1+exp(-(Topt-x)/Bh))*H))
			frm<- "m(T) = 1 - 1/(exp((1+exp(-(x-Topt)/Bl)).(1+exp(-(Topt-x)/Bh)).H))"
		}

		if(modelNumber==29){
			f <- function(x,Tl,Th,Bl,Bh,H)
			{
				expr <- expression(1 - 1/(exp((1+exp(-(x-Tl)/Bl))*(1+exp(-(Th-x)/Bh))*H)))
				eval(expr)
			}
			j <- function(x,Tl,Th,Bl,Bh,H)
			{
				expr <- expression(1 - 1/(exp((1+exp(-(x-Tl)/Bl))*(1+exp(-(Th-x)/Bh))*H)))
				c(eval(D(expr, "Tl")),eval(D(expr, "Th")),eval(D(expr, "Bl")),eval(D(expr, "Bh")), eval(D(expr, "H")))
			}
			g<-y ~ 1 - 1/(exp((1+exp(-(x-Tl)/Bl))*(1+exp(-(Th-x)/Bh))*H))
			frm<- "m(T) = 1 - 1/(exp((1+exp(-(x-Tl)/Bl)).(1+exp(-(Th-x)/Bh)).H))" 
		}



		if(modelNumber==30){
			f <- function(x,Topt,B,H)
			{
				expr <- expression(1 - H/(exp(1+exp(-(x-Topt)/B))*(1+exp(-(Topt-x)/B))))
				eval(expr)
			}
			j <- function(x,Topt,B,H)
			{
				expr <- expression(1 - H/(exp(1+exp(-(x-Topt)/B))*(1+exp(-(Topt-x)/B))))
				c(eval(D(expr, "Topt")),eval(D(expr, "B")), eval(D(expr, "H")))
			}
			g<-y ~ 1 - H/(exp(1+exp(-(x-Topt)/B))*(1+exp(-(Topt-x)/B)))
			frm<- "m(T) = 1 - H/(exp(1+exp(-(x-Topt)/B)).(1+exp(-(Topt-x)/B)))" 
		}


		if(modelNumber==31){
			f <- function(x,Tl,Th,B,H)
			{
				expr <- expression(1 - H/(exp(1+exp(-(x-Tl)/B))*(1+exp(-(Th-x)/B))))
				eval(expr)
			}
			j <- function(x,Tl,Th,B,H)
			{
				expr <- expression(1 - H/(exp(1+exp(-(x-Tl)/B))*(1+exp(-(Th-x)/B))))
				c(eval(D(expr, "Tl")),eval(D(expr, "Th")), eval(D(expr, "B")), eval(D(expr, "H")))
			}
			g<-y ~ 1 - H/(exp(1+exp(-(x-Tl)/B))*(1+exp(-(Th-x)/B)))
			frm<- "m(T) = 1 - H/(exp(1+exp(-(x-Tl)/B))*(1+exp(-(Th-x)/B)))" 
		}

		if(modelNumber==32){
			f <- function(x,Topt,Bl,Bh,H)
			{
				expr <- expression(1 - H/(exp(1+exp(-(x-Topt)/Bl))*(1+exp(-(Topt-x)/Bh))))
				eval(expr)
			}
			j <- function(x,Topt,Bl,Bh,H)
			{
				expr <- expression(1 - H/(exp(1+exp(-(x-Topt)/Bl))*(1+exp(-(Topt-x)/Bh))))
				c(eval(D(expr, "Topt")),eval(D(expr, "Bl")),eval(D(expr, "Bh")), eval(D(expr, "H")))
			}
			g<-y ~ 1 - H/(exp(1+exp(-(x-Topt)/Bl))*(1+exp(-(Topt-x)/Bh)))
			frm<- "m(T) = 1 - H/(exp(1+exp(-(x-Topt)/Bl)).(1+exp(-(Topt-x)/Bh)))" 
		}


		if(modelNumber==33){
			f <- function(x,Tl,Th,Bl,Bh,H)
			{
				expr <- expression(1 - H/(exp(1+exp(-(x-Tl)/Bl))*(1+exp(-(Th-x)/Bh))))
				eval(expr)
			}
			j <- function(x,Tl,Th,Bl,Bh,H)
			{
				expr <- expression(1 - H/(exp(1+exp(-(x-Tl)/Bl))*(1+exp(-(Th-x)/Bh))))
				c(eval(D(expr, "Tl")),eval(D(expr, "Th")),eval(D(expr, "Bl")),eval(D(expr, "Bh")), eval(D(expr, "H")))
			}
			g<-y ~ 1 - H/(exp(1+exp(-(x-Tl)/Bl))*(1+exp(-(Th-x)/Bh)))
			frm<- "m(T) = 1 - H/(exp(1+exp(-(x-Tl)/Bl)).(1+exp(-(Th-x)/Bh)))" 
		}



		if(modelNumber==34){
			f <- function(x,Hl,Tl,Hh,Th,Bm)
			{
				expr <- expression(Bm + ((1-1/(1+exp((Hl/1.987)*((1/Tl)-(1/(x+273.15))))+exp((Hh/1.987)*((1/Th)-(1/(x+273.15))))))*(1-Bm))) #DM: se agrego 2 parentesis "Bm + (...)*"
				eval(expr)
			}
			j <- function(x,Hl,Tl,Hh,Th,Bm)
			{
				expr <- expression((Bm + (1-1/(1+exp((Hl/1.987)*((1/Tl)-(1/(x+273.15))))+exp((Hh/1.987)*((1/Th)-(1/(x+273.15))))))*(1-Bm))) #DM: se agrego 2 parentesis "Bm + (...)*"
				c(eval(D(expr, "Hl")),eval(D(expr, "Tl")),eval(D(expr, "Hh")),eval(D(expr, "Th")), eval(D(expr, "Bm")))
			}
			g<-y ~ Bm + ((1-1/(1+exp((Hl/1.987)*((1/Tl)-(1/(x+273.15))))+exp((Hh/1.987)*((1/Th)-(1/(x+273.15))))))*(1-Bm)) #DM: se agrego 2 parentesis "Bm + (...)*"
			frm<- "m(T) = Bm + ((1-1/(1+exp((Hl/1.987).((1/Tl)-(1/x)))+exp((Hh/1.987)*((1/Th)-(1/x))))).(1-Bm))"  #DM: se agrego 2 parentesis "Bm + (...)*"
		}



		if(modelNumber==35){
			f <- function(x,a1,b1,a2,b2)
			{
				expr <- expression((1 - exp(-(exp(a1+b1*x)))) + (1 - exp(-(exp(a2+b2*x)))))
				eval(expr)
			}
			j <- function(x,a,b)
			{
				expr <- expression((1 - exp(-(exp(a1+b1*x)))) + (1 - exp(-(exp(a2+b2*x)))))
				c(eval(D(expr, "a1")),eval(D(expr, "b1")),eval(D(expr, "a2")),eval(D(expr, "b2")))
			}
			g<-y ~ (1 - exp(-(exp(a1+b1*x)))) + (1 - exp(-(exp(a2+b2*x))))
			frm<- "m(T) = (1 - exp(-(exp(a1+b1.T)))) + (1 - exp(-(exp(a2+b2.T))))"
		}


		if(modelNumber==36){
			f <- function(x,w)
			{
				expr <- expression((w-x)^(-1))
				eval(expr)
			}
			j <- function(x,w)
			{
				expr <- expression((w-x)^(-1))
				c(eval(D(expr, "w")))
			}
			g<-y ~ (w-x)^(-1)
			frm<- "m(T) = (w-x)^(-1)"
		}


		if(modelNumber==37){
			f <- function(x,a1,b1,a2,b2)
			{
				expr <- expression(a1*exp(b1*x) + a2*exp(b2*x))
				eval(expr)
			}
			j <- function(x,a1,b1,a2,b2)
			{
				expr <- expression(a1*exp(b1*x) + a2*exp(b2*x))
				c(eval(D(expr, "a1")),eval(D(expr, "b1")),eval(D(expr, "a2")),eval(D(expr, "b2")))
			}
			g<-y ~ a1*exp(b1*x) + a2*exp(b2*x)
			frm<- "m(T) = a1.exp(b1.x) + a2.exp(b2.x)" 
		}

		if(modelNumber==38){
			f <- function(x,a1,b1,a2,b2,c1)
			{
				expr <- expression(a1*exp(b1*x) + a2*exp(b2*x) + c1)
				eval(expr)
			}
			j <- function(x,a1,b1,a2,b2,c1)
			{
				expr <- expression(a1*exp(b1*x) + a2*exp(b2*x) + c1)
				c(eval(D(expr, "a1")),eval(D(expr, "b1")),eval(D(expr, "a2")),eval(D(expr, "b2")),eval(D(expr, "c1")))
			}
			g<-y ~ a1*exp(b1*x)+c1
			frm<- "m(T) = a1.exp(b1.x) + a2.exp(b2.x) + c1"
		}


		if(modelNumber==39){
			f <- function(x,a,b,nn)
			{
				expr <- expression(a*(abs(x-b))^nn)
				eval(expr)
			}
			j <- function(x,a,b,nn)
			{
				expr <- expression(a*(abs(x-b))^nn)
				c(eval(D(expr, "a")),eval(D(expr, "b")),eval(D(expr, "nn")))
			}
			g<-y ~ a*(abs(x-b))^nn
			frm<- "m(T) = a.(abs(x-b))^nn"
		}



		if(modelNumber==40){
			f <- function(x,a,To,Tl,d)
			{
				expr <- expression(a*x*(x-To)*(Tl-x)^(1/d))
				eval(expr)
			}
			j <- function(x,a,To,Tl,d) #DM: Se corrigió, antes tenia los parametros del modelo 39
			{
				expr <- expression(a*x*(x-To)*(Tl-x)^(1/d))
				c(eval(D(expr, "a")),eval(D(expr, "To")),eval(D(expr, "Tl")),eval(D(expr, "d")))
			}
			g<-y ~ a*x*(x-To)*(Tl-x)^(1/d)
			frm<- "m(T) = a.x.(x-To).(Tl-x)^(1/d)" 
		}


		if(modelNumber==41){
			f <- function(x,a,To,Tl,d)
			{
				expr <- expression(exp(a*x*(x-To)*(Tl-x)^(1/d)))
				eval(expr)
			}
			j <- function(x,a,To,Tl,d) #DM: Se corrigió, antes tenia los parametros del modelo 39
			{
				expr <- expression(exp(a*x*(x-To)*(Tl-x)^(1/d)))
				c(eval(D(expr, "a")),eval(D(expr, "To")),eval(D(expr, "Tl")),eval(D(expr, "d")))
			}
			g<-y ~ exp(a*x*(x-To)*(Tl-x)^(1/d))
			frm<- "m(T) = exp(a.x.(x-To).(Tl-x)^(1/d))"
		}


		if(modelNumber==42){
			f <- function(x,a,Tmax, Tmin,n,m)
			{
				expr <- expression(a*((x-Tmin)^n)*(Tmax-x)^m)
				eval(expr)
			}
			j <- function(x,a,Tmax, Tmin,n,m)
			{
				expr <- expression(a*((x-Tmin)^n)*(Tmax-x)^m)
				c(eval(D(expr, "a")),eval(D(expr, "Tmax")),eval(D(expr, "Tmin")),eval(D(expr, "n")),eval(D(expr, "m")))
			}
			g<-y ~ a*((x-Tmin)^n)*(Tmax-x)^m
			frm<- "m(T) = a.((x-Tmin)^n).(Tmax-x)^m"
		}

		if(modelNumber==43){
			f <- function(x,Dmin,k,Tp,lamb)
			{
				expr <- expression(1/((Dmin/2) * (exp(k*(x-Tp)) + exp(-(x-Tp)*lamb))))
				eval(expr)
			}
			j <- function(x,Dmin,k,Tp,lamb)
			{
				expr <- expression(1/((Dmin/2) * (exp(k*(x-Tp)) + exp(-(x-Tp)*lamb))))
				c(eval(D(expr, "Dmin")),eval(D(expr, "k")),eval(D(expr, "Tp")),eval(D(expr, "lamb")))
			}
			g<-y ~ 1/((Dmin/2) * (exp(k*(x-Tp)) + exp(-(x-Tp)*lamb)))
			frm<- "m(T) = 1/((Dmin/2) . (exp(k.(x-Tp)) + exp(-(x-Tp).lamb)))"
		}


		if(modelNumber==44){
			f <- function(x,a,Tl, Th,B)
			{
				expr <- expression(a*(1-exp(-(x-Tl)/B))*(1-exp(-(Th-x)/B)))
				eval(expr)
			}
			j <- function(x,a,Tl, Th,B)
			{
				expr <- expression(a*(1-exp(-(x-Tl)/B))*(1-exp(-(Th-x)/B)))
				c(eval(D(expr, "a")),eval(D(expr, "Tl")),eval(D(expr, "Th")),eval(D(expr, "B")))
			}
			g<-y ~ a*(1-exp(-(x-Tl)/B))*(1-exp(-(Th-x)/B))
			frm<- "m(T) = a.(1-exp(-(x-Tl)/B)).(1-exp(-(Th-x)/B))"
		}

		if(modelNumber==45){
			f <- function(x,a,Tl, Th,Bl,Bh)
			{
				expr <- expression(exp(a*(1-exp(-(x-Tl)/Bl))*(1-exp(-(Th-x)/Bh))))
				eval(expr)
			}
			j <- function(x,a,Tl, Th,Bl,Bh)
			{
				expr <- expression(exp(a*(1-exp(-(x-Tl)/Bl))*(1-exp(-(Th-x)/Bh))))
				c(eval(D(expr, "a")),eval(D(expr, "Tl")),eval(D(expr, "Th")),eval(D(expr, "Bl")),eval(D(expr, "Bh")))
			}
			g<-y ~ exp(a*(1-exp(-(x-Tl)/Bl))*(1-exp(-(Th-x)/Bh)))
			frm<- "m(T) = exp(a.(1-exp(-(x-Tl)/Bl)).(1-exp(-(Th-x)/Bh)))"
		}
	
    
    fcn     <- function(ini, x, y, fcall, jcall)
      (y - do.call("fcall", c(list(x = x), ini)))
    out <- nls.lm(par = ini, fn = fcn,fcall = f, jcall = j,x = x, y = y,control  = nls.lm.control(maxiter = 500)) #function use for parameter estimation using Marquardtr method
    estimate<-as.data.frame(out$par)
    stdmor<-diag(ginv(out$hessian))
    return(list(estimatedCoef=estimate,f=g,model=frm,stdmort=stdmor,ecua=frm))
  }
    
  
}

coef_mort<-function(modelNumber,coefEstimated,stdmortg,model,gg,datm,alg,weight,weights)
##########################################################################################################
 # this function use as input a model and his estimated parameter and return statistics(R2, AIC,r_ajus,RMSE, ...) 
 #can be used to compare and select a model among many other.
 #
 #modelNumber,coefEstimated (coef of the model estimated),stdmortg,model(output obtained when calling nls function to estimate parameters) ,gg(formula)
 # 
##########################################################################################################
{
  x<-datm[,1]
  y<-datm[,2]
  ind <- as.list(coefEstimated)
  for (i in names(ind))
  {
    temp <- coefEstimated[[i]]
    storage.mode(temp) <- "double"
    assign(i, temp)
    
  }
  if(alg=="Newton")
  {
    if(modelNumber==1 ||modelNumber==2 || modelNumber==3 || modelNumber==4 || modelNumber==5 || modelNumber==6)
    {
      yl<-predict(model)
      art=0.00000000000000000000000000001
      sqe <- sum(residuals(model)^2)+art
      qq<-summary(model)
      tabla<-qq$"parameters"
      stderror<-tabla[,2]  #Agrege DM
      aic<-AIC(model) #Quite ,k=length(coefEstimated)  DM
	  RMSE <- sqrt(mean(residuals(model)^2)) #evaluation of the Root mean square
      if(weight=="WLS") r<-1-sum(residuals(model)^2)/sum(weights*(y-mean(y))^2)
      if(weight=="LS")  r<-1-sum(residuals(model)^2)/sum((y-mean(y))^2)
      r_ajus<- 1 - ((length(x) - 1) / (length(x) - length(coefEstimated))) * (1-r)
      if(weight=="WLS") MSC<-log(sum(weights*(yl-mean(yl))^2)/sum(weights*(y-mean(y))^2))-2*length(coefEstimated)/length(x)
      if(weight=="LS") MSC<-log(sum((yl-mean(yl))^2)/sum((y-mean(y))^2))-2*length(coefEstimated)/length(x)
      
      anva.1 <- c(length(coef(model))-1,length(x)-length(coef(model)),length(x)-1)
      anva.2 <- c((sum(y^2)-length(y)*mean(y)^2)-sqe,sqe,sum(y^2)-length(y)*mean(y)^2)
      anva.3 <- c(anva.2[1]/anva.1[1],anva.2[2]/anva.1[2],NA)
      anva.4 <- c(anva.3[1]/anva.3[2],NA,NA)
      anva.5 <- c(1-pf(anva.4[1],anva.1[1],anva.1[2]),NA,NA)
      anva   <- cbind(anva.1,round(anva.2,4),round(anva.3,4),round(anva.4,4),round(anva.5,4))
      rownames(anva) <- c("Model","Error","Total")
      colnames(anva) <- c("DF","SS","MS","Fc","p")
     
        cat("MORTALITY FOR TEMPERATURE\n")
        dat <- data.frame(T = datm[,1], Mortality = datm[,2])
     
	 
  #    print(dat)
      cat("\nNONLINEAR REGRESSION MODEL\n")
      cat("\nMethod:", alg)
      cat("\nFormula: ", paste(gg[2],gg[1],gg[3]),"\n")
      cat("\nParameters\n")
      print(round(qq$"parameters",5))
      cat("\nAnalysis of Variance\n")
      print(anva,na.print = "")
      
      FRAMESEL<-data.frame(R2=round(r,4),R2_Adj=round(r_ajus,4),SSR=round(sqe,4),AIC=round(aic,4),MSC=round(MSC,4), RMSE = round(RMSE,4))
      colnames(FRAMESEL)[4]<-nombre # Nuevo DM
      rownames(FRAMESEL)<-c("")
      cat("\nSelection criteria")
      print(t(FRAMESEL))
      para<-t(matrix(paste(round(coefEstimated,5),"(","±",round(as.numeric(tabla[,2]),5),")")))
      colnames(para)<-names(coefEstimated)
      param<-data.frame(models =paste(gg[2],gg[1],gg[3]),para,d.f.=paste(anva.1[1],",",anva.1[2]),F=round(anva.4[1],3),P.value=round(anva.5[1],3),FRAMESEL)
    }
  }
  
  if(alg=="Marquardtr")
  {
		if(modelNumber==1)   
		{
		  expre<-expression(a*x^2+b*x+c)
		  yl<-eval(expre)
		  gg<-y~a*x^2+b*x+c
		}
		if(modelNumber==2){
		  expre<-expression(a*sqrt(x)+b*x+c)
		  yl<-eval(expre)
		  gg<-y~a*sqrt(x)+b*x+c
		} 
		if(modelNumber==3)   
		{
		  expre<-expression(a*(1/sqrt(x))+b*x+c)
		  yl<-eval(expre)
		  gg<-y~a*(1/sqrt(x))+b*x+c
		}
		if(modelNumber==4){
		  expre<-expression(a*(1/x^2)+b*x+c)
		  yl<-eval(expre)
		  gg<-y~a*(1/x^2)+b*x+c
		}
		if(modelNumber==5){
		  expre<-expression(a*(1/x)+b*x+c)
		  yl<-eval(expre)
		  gg<-y~a*(1/x)+b*x+c
		}
		if(modelNumber==6){
		  expre<-expression(b*x+a*log(x)+c)
		  yl<-eval(expre)
		  gg<-y~b*x+a*log(x)+c
		}
		
		if(modelNumber==7){
		  expre<-expression(1/(1+a*exp(-b*((x-c)/d)^2)))
		  yl<-eval(expre)
		  gg<-y~1/(1+a*exp(-b*((x-c)/d)^2))
		}

		
		if(modelNumber==8) {
		  expre<-expression(a*exp(-b*((x-xo)/c)^2))
		  yl<-eval(expre)
		  gg<-y~a*exp(-b*((x-xo)/c)^2)
		}
		
		if(modelNumber==9)   
		{
		  expre<-expression(y0 + a * exp(-0.5 * ((x-x0)/b)^2))
		  yl<-eval(expre)
		  gg<-y~y0 + a * exp(-0.5 * ((x-x0)/b)^2)
		}
	
		if(modelNumber==10) {
			expre<-expression(y0 + a * exp(-0.5 * (log(abs(x/x0))/b)^2))
			yl<-eval(expre)
			gg<-y~y0 + a * exp(-0.5 * (log(abs(x/x0))/b)^2)
		}

		if(modelNumber==11) {
			expre<-expression(b1+b2*x+b3*x^d)
			yl<-eval(expre)
			gg<-y~b1+b2*x+b3*x^d
		}

		if(modelNumber==12) {
			expre<-expression(exp(b1+b2*x+b3*x^2))
			yl<-eval(expre)
			gg<-y~exp(b1+b2*x+b3*x^2)
		}

		if(modelNumber==13) {
			expre<-expression(1-(b4/(1+b5*exp(b1+b2*x+b3*x^2))))
			yl<-eval(expre)
			gg<-y~1-(b4/(1+b5*exp(b1+b2*x+b3*x^2)))
		}


		if(modelNumber==14) {
			expre<-expression(exp(b1+b2*x+b3*sqrt(x)))
			yl<-eval(expre)
			gg<-y~exp(b1+b2*x+b3*sqrt(x))
		}


		if(modelNumber==15) {
			expre<-expression(1-(b4/(1+b5*exp(b1+b2*x+b3*sqrt(x)))))
			yl<-eval(expre)
			gg<-y~1-(b4/(1+b5*exp(b1+b2*x+b3*sqrt(x))))
		}

		if(modelNumber==16) {
			expre<-expression(exp(b1+b2*x+b3*(1/sqrt(x))))
			yl<-eval(expre)
			gg<-y~exp(b1+b2*x+b3*(1/sqrt(x)))
		}

		if(modelNumber==17) {
			expre<-expression(1-(b4/(1+b5*exp(b1+b2*x+b3*(1/sqrt(x))))))
			yl<-eval(expre)
			gg<-y~1-(b4/(1+b5*exp(b1+b2*x+b3*(1/sqrt(x)))))
		}

		if(modelNumber==18) {
			expre<-expression(exp(b1+b2*x+b3*(1/x)))
			yl<-eval(expre)
			gg<-y~exp(b1+b2*x+b3*(1/x))
		}

		if(modelNumber==19) {
			expre<-expression(1-(b4/(1+b5*exp(b1+b2*x+b3*(1/x)))))
			yl<-eval(expre)
			gg<-y~1-(b4/(1+b5*exp(b1+b2*x+b3*(1/x))))
		}

		if(modelNumber==20) {
			expre<-expression(exp(b1+b2*x+b3*x^d))
			yl<-eval(expre)
			gg<-y~exp(b1+b2*x+b3*x^d)
		}

		if(modelNumber==21) {
			expre<-expression(1-(b4/(1+b5*exp(b1+b2*x+b3*x^d))))
			yl<-eval(expre)
			gg<-y~1-(b4/(1+b5*exp(b1+b2*x+b3*x^d)))
		}

		if(modelNumber==22) {
			expre<-expression(exp(b1+b2*x+b3*log(x)))
			yl<-eval(expre)
			gg<-y~exp(b1+b2*x+b3*log(x))
		}

		if(modelNumber==23) {
			expre<-expression(1-(b4/(1+b5*exp(b1+b2*x+b3*log(x)))))
			yl<-eval(expre)
			gg<-y~1-(b4/(1+b5*exp(b1+b2*x+b3*log(x))))
		}

		if(modelNumber==24) {
			expre<-expression(1-rm*exp((-0.5)*(-(x-Topt)/Troh)^2))
			yl<-eval(expre)
			gg<-y~1-rm*exp((-0.5)*(-(x-Topt)/Troh)^2)
		}

		if(modelNumber==25) {
			expre<-expression(1-rm*exp((-0.5)*(-(log(x)-log(Topt))/Troh)^2))
			yl<-eval(expre)
			gg<-y~1-rm*exp((-0.5)*(-(log(x)-log(Topt))/Troh)^2)
		}

		if(modelNumber==26) {
			expre<-expression(1 - 1/(exp((1+exp(-(x-Topt)/B))*(1+exp(-(Topt-x)/B))*H)))
			yl<-eval(expre)
			gg<-y~1 - 1/(exp((1+exp(-(x-Topt)/B))*(1+exp(-(Topt-x)/B))*H))
		}

		if(modelNumber==27) {
			expre<-expression(1 - 1/(exp((1+exp(-(x-Tl)/B))*(1+exp(-(Th-x)/B))*H)))
			yl<-eval(expre)
			gg<-y~1 - 1/(exp((1+exp(-(x-Tl)/B))*(1+exp(-(Th-x)/B))*H))
		}

		if(modelNumber==28) {
			expre<-expression(1 - 1/(exp((1+exp(-(x-Topt)/Bl))*(1+exp(-(Topt-x)/Bh))*H)))
			yl<-eval(expre)
			gg<-y~1 - 1/(exp((1+exp(-(x-Topt)/Bl))*(1+exp(-(Topt-x)/Bh))*H))
		}

		if(modelNumber==29) {
			expre<-expression(1 - 1/(exp((1+exp(-(x-Tl)/Bl))*(1+exp(-(Th-x)/Bh))*H)))
			yl<-eval(expre)
			gg<-y~1 - 1/(exp((1+exp(-(x-Tl)/Bl))*(1+exp(-(Th-x)/Bh))*H))
		}


		if(modelNumber==30) {
			expre<-expression(1 - H/(exp(1+exp(-(x-Topt)/B))*(1+exp(-(Topt-x)/B))))
			yl<-eval(expre)
			gg<-y~1 - H/(exp(1+exp(-(x-Topt)/B))*(1+exp(-(Topt-x)/B)))
		}

		if(modelNumber==31) {
			expre<-expression(1 - H/(exp(1+exp(-(x-Tl)/B))*(1+exp(-(Th-x)/B))))
			yl<-eval(expre)
			gg<-y~1 - H/(exp(1+exp(-(x-Tl)/B))*(1+exp(-(Th-x)/B)))
		}

		if(modelNumber==32) {
			expre<-expression(1 - H/(exp(1+exp(-(x-Topt)/Bl))*(1+exp(-(Topt-x)/Bh))))
			yl<-eval(expre)
			gg<-y~1 - H/(exp(1+exp(-(x-Topt)/Bl))*(1+exp(-(Topt-x)/Bh)))
		}

		if(modelNumber==33) {
			expre<-expression(1 - H/(exp(1+exp(-(x-Tl)/Bl))*(1+exp(-(Th-x)/Bh))))
			yl<-eval(expre)
			gg<-y~1 - H/(exp(1+exp(-(x-Tl)/Bl))*(1+exp(-(Th-x)/Bh)))
		}

		if(modelNumber==34) {
			expre<-expression(Bm + ((1-1/(1+exp((Hl/1.987)*((1/Tl)-(1/(x+273.15))))+exp((Hh/1.987)*((1/Th)-(1/(x+273.15))))))*(1-Bm))) #DM: se agrego 2 parentesis "Bm + (...)*"
			yl<-eval(expre)
			gg<-y~Bm + ((1-1/(1+exp((Hl/1.987)*((1/Tl)-(1/(x+273.15))))+exp((Hh/1.987)*((1/Th)-(1/(x+273.15))))))*(1-Bm)) #DM: se agrego 2 parentesis "Bm + (...)*"
		}

		if(modelNumber==35) {
			expre<-expression((1 - exp(-(exp(a1+b1*x)))) + (1 - exp(-(exp(a2+b2*x)))))
			yl<-eval(expre)
			gg<-y~(1 - exp(-(exp(a1+b1*x)))) + (1 - exp(-(exp(a2+b2*x))))
		}

		if(modelNumber==36) {
			expre<-expression((w-x)^(-1))
			yl<-eval(expre)
			gg<-y ~ (w-x)^(-1)
		}

		if(modelNumber==37) {
			expre<-expression(a1*exp(b1*x) + a2*exp(b2*x))
			yl<-eval(expre)
			gg<-y ~ a1*exp(b1*x) + a2*exp(b2*x)
		}

		if(modelNumber==38) {
			expre<-expression(a1*exp(b1*x) + a2*exp(b2*x) + c1)
			yl<-eval(expre)
			gg<-y ~ a1*exp(b1*x) + a2*exp(b2*x) + c1
		}


		if(modelNumber==39) {
			expre<-expression(a*(abs(x-b))^nn)
			yl<-eval(expre)
			gg<-y ~ a*(abs(x-b))^nn
		}


		if(modelNumber==40) {
			expre<-expression(a*x*(x-To)*(Tl-x)^(1/d))
			yl<-eval(expre)
			gg<-y ~ a*x*(x-To)*(Tl-x)^(1/d)
		}


		if(modelNumber==41) {
			expre<-expression(exp(a*x*(x-To)*(Tl-x)^(1/d)))
			yl<-eval(expre)
			gg<-y ~ exp(a*x*(x-To)*(Tl-x)^(1/d))
		}

		if(modelNumber==42) {
			expre<-expression(a*((x-Tmin)^n)*(Tmax-x)^m)
			yl<-eval(expre)
			gg<-y ~ a*((x-Tmin)^n)*(Tmax-x)^m
		}

		if(modelNumber==43) {
			expre<-expression(1/((Dmin/2) * (exp(k*(x-Tp)) + exp(-(x-Tp)*lamb))))
			yl<-eval(expre)
			gg<-y ~ 1/((Dmin/2) * (exp(k*(x-Tp)) + exp(-(x-Tp)*lamb)))
		}

		if(modelNumber==44) {
			expre<-expression(a*(1-exp(-(x-Tl)/B))*(1-exp(-(Th-x)/B)))
			yl<-eval(expre)
			gg<-y ~ a*(1-exp(-(x-Tl)/B))*(1-exp(-(Th-x)/B))
		}

		if(modelNumber==45) {
			expre<-expression(exp(a*(1-exp(-(x-Tl)/Bl))*(1-exp(-(Th-x)/Bh))))
			yl<-eval(expre)
			gg<-y ~ exp(a*(1-exp(-(x-Tl)/Bl))*(1-exp(-(Th-x)/Bh)))
		}	
    
	
    sqe <- sum((y-yl)^2)
    if(length(x) == length(coefEstimated)) stop("number of parameters = number of temperatures") #DM se agrego!
    var <- (sum((y-yl)^2)/(length(x) - length(coefEstimated)))
    stderror<-sqrt(var*stdmortg)
    
    names(stderror)<-names(coefEstimated) # Agregue DM
    tvalues<-coefEstimated/stderror
    tvalta<-qt(0.025,length(x) - length(coefEstimated))
    pvalues<-1-pt(as.numeric(tvalues),length(x) - length(coefEstimated))
    r<-1-sqe/sum((y-mean(y))^2)
    r_ajus<- 1 - ((length(x) - 1) / (length(x) - length(coefEstimated))) * (1-r)
    
    AC<-length(x)*(log(2*pi*sqe/length(x))+1)+2*(length(coefEstimated)+1) 
    MSC<-log(sum((yl-mean(yl))^2)/sum((y-mean(y))^2))-2*length(coefEstimated)/length(x)
	RMSE <- sqrt(mean((y-yl)^2)) #evaluation of the Root mean square
    anva.1 <- c(length(coefEstimated)-1,length(x)-length(coefEstimated),length(x)-1)
    anva.2 <- c((sum(y^2)-length(y)*mean(y)^2)-sqe,sqe,sum(y^2)-length(y)*mean(y)^2)
    anva.3 <- c(anva.2[1]/anva.1[1],anva.2[2]/anva.1[2],NA)
    anva.4 <- c(anva.3[1]/anva.3[2],NA,NA)
    anva.5 <- c(1-pf(anva.4[1],anva.1[1],anva.1[2]),NA,NA)
    anva   <- cbind(anva.1,round(anva.2,4),round(anva.3,4),round(anva.4,4),round(anva.5,4))
    rownames(anva) <- c("Model","Error","Total")
    colnames(anva) <- c("DF","SS","MS","Fc","p")
    
	
	
    cat("MORTALITY FOR TEMPERATURE\n")
    dat <- data.frame(T = datm[,1], Mortality = datm[,2])
 
    cat("\nNONLINEAR REGRESSION MODEL\n")
    cat("\nMethod:", alg)
    cat("\nFormula: ", paste(gg[2],gg[1],gg[3]),"\n")
     cat("\nParameters\n")
     estshap2<-data.frame(matrix(round(coefEstimated,4)),round(stderror,5),formatC(round(as.numeric(tvalues),5)),round(pvalues,5))
     colnames(estshap2)<-c("Estimate","Std.Error","t value","Pr(>|t|)")
     rownames(estshap2)<-c(colnames(coefEstimated))
     print(estshap2)
     cat("\nAnalysis of Variance\n")
     print(anva,na.print = "")
    n<-length(x); k<-length(coefEstimated); if(n/k<40) {AC<-AC+2*k*(k+1)/(n-k-1); nombre="AICc"} else nombre="AIC" #Nuevo DM
     FRAMESEL<-data.frame(R2=round(r,4),R2_Adj=round(r_ajus,4),SSR=round(sqe,4),AIC=round(AC,4),MSC=round(MSC,4),RMSE=round(RMSE,4))
    colnames(FRAMESEL)[4]<-nombre 
     rownames(FRAMESEL)<-c("")
     cat("\nSelection criteria")
     print(t(FRAMESEL))
    para<-t(matrix(paste(round(coefEstimated,5),"(","±",round(stderror,5),")")))
    colnames(para)<-names(coefEstimated)
    param<-data.frame(models = paste(gg[2],gg[1],gg[3]),para,d.f.=paste(anva.1[1],",",anva.1[2]),F=round(anva.4[1],3),P.value=round(anva.5[1],3),FRAMESEL)
    
	
  }
  return(list(parmor=param,frames=FRAMESEL, Std.Error=stderror))      #
  
}




grafmort <- function( modelNumber, coefEstimated, g, datm, corrx=NULL, corry=NULL, mini, maxi, limit,tam,labx, laby,titulo,grises=FALSE)
  {
  
  ##############################################################################################################"
  #  this function  take in input a model with a set of other parameter will be used to plot the final graph of  
  #select funtion using estimated parameter
  #
  ######################################################################################################################
   if(grises==TRUE){ccol=c("gray20","gray30")}else{ccol=c(4,2)}
  data<-datm
  x<-data[,1]
 estshap<-as.list(coefEstimated)

  finalshap<-estshap
  for (i in names(finalshap)) {
    temp <- finalshap[[i]]
    storage.mode(temp) <- "double"
    assign(i, temp)
  }
  xl<-seq(mini,maxi,length=2000)
  
  
#  if(modelNumber==1 ||modelNumber==2 || modelNumber==3 || modelNumber==4 || modelNumber==5 || modelNumber==6)
#  {
#    if(modelNumber==1)  fom<-y~a*x^2+b*x+c
#    if(modelNumber==2)  fom<-y~a*sqrt(x)+b*x+c
#    if(modelNumber==3)  fom<-y~a*(1/sqrt(x))+b*x+c
#    if(modelNumber==4)  fom<-y~a*(1/x^2)+b*x+c
#    if(modelNumber==5)  fom<-y~a*(1/x)+b*x+c
#    if(modelNumber==6)  fom<-y~b*x+a*log(x)+c
#    forex <- fom[[length(fom)]]
#    ylexpx<-as.expression(forex)
#    ylx<-eval(ylexpx)
#    x<-xl
#    ylexp<-as.expression(forex)
#    yl<-eval(ylexp)
#  }
  
	if(modelNumber==1) {
			exprex<-expression(a*x^2+b*x+c)
			ylx<-eval(exprex)
			expre<-expression(a*xl^2+b*xl+c)
			yl<-eval(expre)
		}

	if(modelNumber==2) {
			exprex<-expression(a*sqrt(x)+b*x+c)
			ylx<-eval(exprex)
			expre<-expression(a*sqrt(xl)+b*xl+c)
			yl<-eval(expre)
		}
		
	if(modelNumber==3) {
			exprex<-expression(a*(1/sqrt(x))+b*x+c)
			ylx<-eval(exprex)
			expre<-expression(a*(1/sqrt(xl))+b*xl+c)
			yl<-eval(expre)
		}
		
	if(modelNumber==4) {
			exprex<-expression(1/(1+a*exp(-b*((x-c)/d)^2)))
			ylx<-eval(exprex)
			expre<-expression(1/(1+a*exp(-b*((xl-c)/d)^2)))
			yl<-eval(expre)
		}
		
	if(modelNumber==5) {
			exprex<-expression(a*(1/x^2)+b*x+c)
			ylx<-eval(exprex)
			expre<-expression(a*(1/xl^2)+b*xl+c)
			yl<-eval(expre)
		}
		
	if(modelNumber==6) {
			exprex<-expression(a*(1/x^2)+b*x+c)
			ylx<-eval(exprex)
			expre<-expression(a*(1/xl^2)+b*xl+c)
			yl<-eval(expre)
		}

	
	if(modelNumber==7) {
		exprex<-expression(1/(1+a*exp(-b*((x-c)/d)^2)))
		ylx<-eval(exprex)
		expre<-expression(1/(1+a*exp(-b*((xl-c)/d)^2)))
		yl<-eval(expre)
	}
	if(modelNumber==8) {
		exprex<-expression(a*exp(-b*((x-xo)/c)^2))
		ylx<-eval(exprex)
		expre<-expression(a*exp(-b*((xl-xo)/c)^2))
		yl<-eval(expre)
	}  
	if(modelNumber==9) {
		exprex<-expression(y0 + a * exp(-0.5 * ((x-x0)/b)^2))
		ylx<-eval(exprex)
		expre<-expression(y0 + a * exp(-0.5 * ((xl-x0)/b)^2))
		yl<-eval(expre)
	}

	if(modelNumber==10) {
		exprex<-expression(y0 + a * exp(-0.5 * (log(abs(x/x0))/b)^2))
		ylx<-eval(exprex)
		expre<-expression(y0 + a * exp(-0.5 * (log(abs(xl/x0))/b)^2))
		yl<-eval(expre)
	}

	if(modelNumber==11) {
		exprex<-expression(b1+b2*x+b3*x^d)
		ylx<-eval(exprex)
		expre<-expression(b1+b2*xl+b3*xl^d)
		yl<-eval(expre)
	}


	if(modelNumber==12) {
		exprex<-expression(exp(b1+b2*x+b3*x^2))
		ylx<-eval(exprex)
		expre<-expression(exp(b1+b2*xl+b3*xl^2))
		yl<-eval(expre)
	}

	if(modelNumber==13) {
		exprex<-expression(1-(b4/(1+b5*exp(b1+b2*x+b3*x^2))))
		ylx<-eval(exprex)
		expre<-expression(1-(b4/(1+b5*exp(b1+b2*xl+b3*xl^2))))
		yl<-eval(expre)
	}

	if(modelNumber==14) {
		exprex<-expression(exp(b1+b2*x+b3*sqrt(x)))
		ylx<-eval(exprex)
		expre<-expression(exp(b1+b2*xl+b3*sqrt(xl)))
		yl<-eval(expre)
	}

	if(modelNumber==15) {
		exprex<-expression(1-(b4/(1+b5*exp(b1+b2*x+b3*sqrt(x)))))
		ylx<-eval(exprex)
		expre<-expression(1-(b4/(1+b5*exp(b1+b2*xl+b3*sqrt(xl)))))
		yl<-eval(expre)
	}

	if(modelNumber==16) {
		exprex<-expression(exp(b1+b2*x+b3*(1/sqrt(x))))
		ylx<-eval(exprex)
		expre<-expression(exp(b1+b2*xl+b3*(1/sqrt(xl))))
		yl<-eval(expre)
	}

	if(modelNumber==17) {
		exprex<-expression(1-(b4/(1+b5*exp(b1+b2*x+b3*(1/sqrt(x))))))
		ylx<-eval(exprex)
		expre<-expression(1-(b4/(1+b5*exp(b1+b2*xl+b3*(1/sqrt(xl))))))
		yl<-eval(expre)
	}

	if(modelNumber==18) {
		exprex<-expression(exp(b1+b2*x+b3*(1/x)))
		ylx<-eval(exprex)
		expre<-expression(exp(b1+b2*xl+b3*(1/xl)))
		yl<-eval(expre)
	}

	if(modelNumber==19) {
		exprex<-expression(1-(b4/(1+b5*exp(b1+b2*x+b3*(1/x)))))
		ylx<-eval(exprex)
		expre<-expression(1-(b4/(1+b5*exp(b1+b2*xl+b3*(1/xl)))))
		yl<-eval(expre)
	}

	if(modelNumber==20) {
		exprex<-expression(exp(b1+b2*x+b3*x^d))
		ylx<-eval(exprex)
		expre<-expression(exp(b1+b2*xl+b3*xl^d))
		yl<-eval(expre)
	}

	if(modelNumber==21) {
		exprex<-expression(1-(b4/(1+b5*exp(b1+b2*x+b3*x^d))))
		ylx<-eval(exprex)
		expre<-expression(1-(b4/(1+b5*exp(b1+b2*xl+b3*xl^d))))
		yl<-eval(expre)
	}

	if(modelNumber==22) {
		exprex<-expression(exp(b1+b2*x+b3*log(x)))
		ylx<-eval(exprex)
		expre<-expression(exp(b1+b2*xl+b3*log(xl)))
		yl<-eval(expre)
	}

	if(modelNumber==23) {
		exprex<-expression(1-(b4/(1+b5*exp(b1+b2*x+b3*log(x)))))
		ylx<-eval(exprex)
		expre<-expression(1-(b4/(1+b5*exp(b1+b2*xl+b3*log(xl)))))
		yl<-eval(expre)
	}

	if(modelNumber==24) {
		exprex<-expression(1-rm*exp((-0.5)*(-(x-Topt)/Troh)^2))
		ylx<-eval(exprex)
		expre<-expression(1-rm*exp((-0.5)*(-(xl-Topt)/Troh)^2))
		yl<-eval(expre)
	}

	if(modelNumber==25) {
		exprex<-expression(1-rm*exp((-0.5)*(-(log(x)-log(Topt))/Troh)^2))
		ylx<-eval(exprex)
		expre<-expression(1-rm*exp((-0.5)*(-(log(xl)-log(Topt))/Troh)^2))
		yl<-eval(expre)
	}

	if(modelNumber==26) {
		exprex<-expression(1 - 1/(exp((1+exp(-(x-Topt)/B))*(1+exp(-(Topt-x)/B))*H)))
		ylx<-eval(exprex)
		expre<-expression(1 - 1/(exp((1+exp(-(xl-Topt)/B))*(1+exp(-(Topt-xl)/B))*H)))
		yl<-eval(expre)
	}

	if(modelNumber==27) {
		exprex<-expression(1 - 1/(exp((1+exp(-(x-Tl)/B))*(1+exp(-(Th-x)/B))*H)))
		ylx<-eval(exprex)
		expre<-expression(1 - 1/(exp((1+exp(-(xl-Tl)/B))*(1+exp(-(Th-xl)/B))*H)))
		yl<-eval(expre)
	}

	if(modelNumber==28) {
		exprex<-expression(1 - 1/(exp((1+exp(-(x-Topt)/Bl))*(1+exp(-(Topt-x)/Bh))*H)))
		ylx<-eval(exprex)
		expre<-expression(1 - 1/(exp((1+exp(-(xl-Topt)/Bl))*(1+exp(-(Topt-xl)/Bh))*H)))
		yl<-eval(expre)
	}

	if(modelNumber==29) {
		exprex<-expression(1 - 1/(exp((1+exp(-(x-Tl)/Bl))*(1+exp(-(Th-x)/Bh))*H)))
		ylx<-eval(exprex)
		expre<-expression(1 - 1/(exp((1+exp(-(xl-Tl)/Bl))*(1+exp(-(Th-xl)/Bh))*H)))
		yl<-eval(expre)
	}

	if(modelNumber==30) {
		exprex<-expression(1 - H/(exp(1+exp(-(x-Topt)/B))*(1+exp(-(Topt-x)/B))))
		ylx<-eval(exprex)
		expre<-expression(1 - H/(exp(1+exp(-(xl-Topt)/B))*(1+exp(-(Topt-xl)/B))))
		yl<-eval(expre)
	}

	if(modelNumber==31) {
		exprex<-expression(1 - H/(exp(1+exp(-(x-Tl)/B))*(1+exp(-(Th-x)/B))))
		ylx<-eval(exprex)
		expre<-expression(1 - H/(exp(1+exp(-(xl-Tl)/B))*(1+exp(-(Th-xl)/B))))
		yl<-eval(expre)
	}

	if(modelNumber==32) {
		exprex<-expression(1 - H/(exp(1+exp(-(x-Topt)/Bl))*(1+exp(-(Topt-x)/Bh))))
		ylx<-eval(exprex)
		expre<-expression(1 - H/(exp(1+exp(-(xl-Topt)/Bl))*(1+exp(-(Topt-xl)/Bh))))
		yl<-eval(expre)
	}

	if(modelNumber==33) {
		exprex<-expression(1 - H/(exp(1+exp(-(x-Tl)/Bl))*(1+exp(-(Th-x)/Bh))))
		ylx<-eval(exprex)
		expre<-expression(1 - H/(exp(1+exp(-(xl-Tl)/Bl))*(1+exp(-(Th-xl)/Bh))))
		yl<-eval(expre)
	}

	if(modelNumber==34) {
		exprex<-expression(Bm + ((1-1/(1+exp((Hl/1.987)*((1/Tl)-(1/(x+273.15))))+exp((Hh/1.987)*((1/Th)-(1/(x+273.15))))))*(1-Bm))) #DM: se agrego 2 parentesis "Bm + (...)*"
		ylx<-eval(exprex)
		expre<-expression(Bm + ((1-1/(1+exp((Hl/1.987)*((1/Tl)-(1/(xl+273.15))))+exp((Hh/1.987)*((1/Th)-(1/(xl+273.15))))))*(1-Bm))) #DM: se agrego 2 parentesis "Bm + (...)*"
		yl<-eval(expre)
	}

	if(modelNumber==35) {
		exprex<-expression((1 - exp(-(exp(a1+b1*x)))) + (1 - exp(-(exp(a2+b2*x)))))
		ylx<-eval(exprex)
		expre<-expression((1 - exp(-(exp(a1+b1*xl)))) + (1 - exp(-(exp(a2+b2*xl)))))
		yl<-eval(expre)
	}


	if(modelNumber==36) {
		exprex<-expression((w-x)^(-1))
		ylx<-eval(exprex)
		expre<-expression((w-xl)^(-1))
		yl<-eval(expre)
	}

	if(modelNumber==37) {
		exprex<-expression(a1*exp(b1*x) + a2*exp(b2*x))
		ylx<-eval(exprex)
		expre<-expression(a1*exp(b1*xl) + a2*exp(b2*xl))
		yl<-eval(expre)
	}

	if(modelNumber==38) {
		exprex<-expression(a1*exp(b1*x) + a2*exp(b2*x) + c1)
		ylx<-eval(exprex)
		expre<-expression(a1*exp(b1*xl) + a2*exp(b2*xl) + c1)
		yl<-eval(expre)
	}

	if(modelNumber==39) {
		exprex<-expression(a*(abs(x-b))^nn)
		ylx<-eval(exprex)
		expre<-expression(a*(abs(xl-b))^nn)
		yl<-eval(expre)
	}

	if(modelNumber==40) {
		exprex<-expression(a*x*(x-To)*(Tl-x)^(1/d))
		ylx<-eval(exprex)
		expre<-expression(a*xl*(xl-To)*(Tl-xl)^(1/d))
		yl<-eval(expre)
	}

	if(modelNumber==41) {
		exprex<-expression(exp(a*x*(x-To)*(Tl-x)^(1/d)))
		ylx<-eval(exprex)
		expre<-expression(exp(a*xl*(xl-To)*(Tl-xl)^(1/d)))
		yl<-eval(expre)
	}

	if(modelNumber==42) {
		exprex<-expression(a*((x-Tmin)^n)*(Tmax-x)^m)
		ylx<-eval(exprex)
		expre<-expression(a*((xl-Tmin)^n)*(Tmax-xl)^m)
		yl<-eval(expre)
	}

	if(modelNumber==43) {
		exprex<-expression(1/((Dmin/2) * (exp(k*(x-Tp)) + exp(-(x-Tp)*lamb))))
		ylx<-eval(exprex)
		expre<-expression(1/((Dmin/2) * (exp(k*(xl-Tp)) + exp(-(xl-Tp)*lamb))))
		yl<-eval(expre)
	}

	if(modelNumber==44) {
		exprex<-expression(a*(1-exp(-(x-Tl)/B))*(1-exp(-(Th-x)/B)))
		ylx<-eval(exprex)
		expre<-expression(a*(1-exp(-(xl-Tl)/B))*(1-exp(-(Th-xl)/B)))
		yl<-eval(expre)
	}

	if(modelNumber==45) {
		exprex<-expression(exp(a*(1-exp(-(x-Tl)/Bl))*(1-exp(-(Th-x)/Bh))))
		ylx<-eval(exprex)
		expre<-expression(exp(a*(1-exp(-(xl-Tl)/Bl))*(1-exp(-(Th-xl)/Bh))))
		yl<-eval(expre)
	}

  
  df<-length(data[,1])-length(estshap)
  sdli<-sqrt(sum((data[,2]-ylx)^2)/df)
  linf<-yl+sdli*qt(0.025,df)
  lsup<-yl-sdli*qt(0.025,df)
  if(limit=="yes")
  {
    
      par(cex=tam)
      if(is.null(corrx)){corrx=c(min(data[,1],0),max(data[,1])*1.2);corrx2=seq(0,10*round(corrx[2]/10,1),5)}else{corrx2=seq(corrx[1],corrx[2],5)} ## cambio
      if(is.null(corry)){corry=c(0,100*(max(data[,2])*1.01));corry2=seq(0,round(max(corry)),10)}else{corry2=seq(corry[1],corry[2],10)}              ## cambio
      
      plot(data[,1],data[,2]*100,frame=F,col=ccol[1],xlim=corrx,ylim=corry,xlab=labx,ylab=laby,pch=19,axes=F,xaxt = "n",main=titulo) ## 4
      
      axis(1, corrx2)  ## cambio
      axis(2, corry2,las=2) ## cambio
      lines(xl, yl*100, lwd=2,col=ccol[2]) ## 2
      lines(xl, linf*100, lwd=1,col=ccol[1],lty=2) ## 4
      lines(xl, lsup*100, lwd=1,col=ccol[1],lty=2) ## 4
      
      valx=xl[100*yl<=100];valx1=min(valx,na.rm=TRUE);valx2=max(valx,na.rm=TRUE) # Se agrego na.rm=TRUE
      return(list(valxs=c(valx1,valx2)))
    
   
  }
  if(limit=="no")
  {
    
      par(cex=tam)
      if(is.null(corrx)){corrx=c(min(data[,1],0),max(data[,1])*1.2);corrx2=seq(0,10*round(corrx[2]/10,1),5)}else{corrx2=seq(corrx[1],corrx[2],5)} ## cambio
      if(is.null(corry)){corry=c(0,100*(max(data[,2])*1.1));corry2=seq(0,round(max(corry)),10)}else{corry2=seq(corry[1],corry[2],10)}              ## cambio
      plot(data[,1],data[,2]*100,frame=F,col=ccol[1],xlim=corrx,ylim=corry,xlab=labx,ylab=laby,pch=19,axes=F,xaxt = "n",main=titulo) ## 4
     
      axis(1, corrx2)  ## cambio
      axis(2, corry2,las=2) ## cambio
      lines(xl, yl*100, lwd=2,col=ccol[2]) ## 2
      valx=xl[100*yl<=100];valx1=min(valx,na.rm=TRUE);valx2=max(valx,na.rm=TRUE) # Se agrego na.rm=TRUE
      return(list(valxs=c(valx1,valx2)))
   
  }
}



chooseTheModel <- function (dirToFile,proc, modelList,alg,initList,mini, maxi, limit, tam, weight,xRange, yRange,xLabel, yLabel,graphTitle)
	
{
##################################################################################################################################
	#This fonction take as input the list of model as input, fit parameter of each of them and plot the result in different windows. As ouput, the model display also various statistic on each model with allow to select the best one.
	
	#modelList : the list of model to fit to data. it is a vector of number refering thes model in the code
	#alg :  fittin algorithm should be use to estimate parameter of the list of models
	#initList : a list of initial value of parameter for model in consideration
##################################################################################################################################
	
	finalOutput = NULL; j = 1
	
	
	datm = formatMortalityData(dirToFile);#,xRange,yRange,xLabel, yLabel,graphTitle)
	
	plot(datm[,1],datm[,2]*100,frame=F,pch=19,col=4,cex=1.3,xlim=xRange,ylim=yRange,axes=F,xaxt = "n",xlab=xLabel,ylab=yLabel,main=graphTitle)
	
	
	if((length(modelList) == length(initList)) )
	{
		for (i in modelList)
			{
				 cat("BEGGINING STUDY OF NEW MODEL \n")
				modelNumber = i ; inim = initList[[j]];
				res=dead_func(modelNumber, datm, alg, inim, weight,weights)
				
				coefEstimated = res$estimatedCoef ;stdmortg = res$stdmort;model = res$model ; gg= res$f
				
				sink(paste("model",as.character(j),".txt"))
				coefs = coef_mort(modelNumber,coefEstimated,stdmortg,model,gg,datm,alg,weight,weights)
				
				cf = coefs$parmor
				rslt = data.frame(ModelName = "sfsd", fleExpression = cf$models, d.f. = cf$d.f.,   F= cf$F, P.value = cf$P.value, R2 = cf$R2,  R2_Adj= cf$R2_Adj, SSR = cf$SSR, AIC= cf$AIC, MSC = cf$MSC, RMSE = cf$RMSE )
				#print(coefs$parmor)

				finalOutput = rbind(finalOutput,rslt)
				cat(finalOutput)
				sink()
				windows(title = paste("model",as.character(j)))
					plot(datm[,1],datm[,2]*100,frame=F,pch=19,col=4,cex=1.3,xlim=xRange,ylim=yRange,axes=F,xaxt = "n",xlab=xLabel,ylab=yLabel,main=graphTitle)
							
					axis(1, xaxp=c(xRange,5))
					axis(2,las=2);
					
										
					grafmort(modelNumber, coefEstimated, g, datm, corrx=NULL, corry=NULL, mini, maxi, limit,tam,xLabel, yLabel, paste("model",as.character(j)),grises=FALSE)
					j = j+1
					
			}

	}
	

	return(finalOutput)

}


recalc<-function(ini,niv=1)
{
  vec<-list(1)
  for(i in 1:length(ini))
  {
    vec[[i]]<-ini[[i]]+ini[[i]]*runif(1,-(niv),niv)
  }
  names(vec)=names(ini)
  return(vec)
}