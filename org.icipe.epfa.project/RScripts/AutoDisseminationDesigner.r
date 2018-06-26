#install.packages("minpack.lm", dependencies = T) #for the function nls.lm()
library(minpack.lm)
library(MASS)
library(dplyr)
#library(geosphere) # 




#'This function estimate the number of traps for one Ha
#'PS: 1Ha = 100*100 m
#'
#'@param interger trap optimum radius in meter
#'@return interger number of trap to controm a farm


#
nbrTrapPerHa <- function(optDist)
{
  # optDist : An interger given the number of crop plant in a given farm
 
  NbrePos = length(seq(optDist/sqrt(2),100,optDist*sqrt(2)))
  
    return(NbrePos*NbrePos);
  
  
}


#'This funciton estimate the number of traps giving the dimension of a farm.
#'@param interger trap optimum radius in meter
#'@param interger farm lenght in meter
#'@param interger farm width in meter
#'@return interger number of trap to controm a farm
#'
estimateNbrTrap <- function(optDist,farmLength,farmWidth)
{
  # optDist : An interger given the number of crop plant in a given farm
  
  NbrePosL = length(seq(optDist/sqrt(2),farmLength,optDist*sqrt(2)))
  NbrePosW = length(seq(optDist/sqrt(2),farmWidth,optDist*sqrt(2)))
  
  return(NbrePosL*NbrePosW);
  
  
}




#'This function take as input the dataFile and provide as output the 
#'data.frame to be used for the modeling process.
#'

formatDisseminationData <- function (dirToFile)
{
  #################################################################################################################
  Dist=1
  Rep=2
  Trapped =4
  released=5
  
  
  dat = read.table(dirToFile, h = T) # reading of data in the file
  
  
  freqTp=aggregate(dat[,Trapped],list(Distance=dat[,Dist],Replicate=dat[,Rep]),sum)
  freqTp
  names(freqTp)[3] = "Freq"
  
  NbrRep = dim(unique(freqTp[Rep]))[1]
  NbrDist = dim(unique(freqTp[Dist]))[1]
  
  freqCum = rep(NA,dim(freqTp)[1])
  for(k in 1:NbrRep)
  {
    freqCum[seq((k-1)*NbrDist+1,k*NbrDist,1)] = rev(cumsum(freqTp[seq(k*NbrDist,(k-1)*NbrDist+1,-1),"Freq"]))
  }
  
  freqTp =data.frame(freqTp,freqCum)
  
  
  datProp = group_by(freqTp, Replicate) %>% mutate(Propt = freqCum/sum(Freq))
  datProp = as.data.frame(datProp)
  
  tablaM = aggregate(datProp[,"Propt"],list(Distance=datProp[,Dist]),mean)
  
  y=round(tablaM[,2],4)
  x=tablaM[,1]
  
  datm=data.frame(x,y)
  
  return(datm);
}







prueba<-function(model,datashap,datao,ini,corrx,corry,punt,labx,laby,titulo, grises=FALSE){
nbY =5
#k=1.25
   if(grises==TRUE){ccol=c("gray10","gray20","gray30")}else{ccol=c(4,1,2)}

    datlinea<-datashap
		plot(datlinea[,1],datlinea[,2],frame=F,pch=19,col=ccol[1],cex=1.3,xlim=corrx,ylim=corry,axes=F,xaxt = "n",xlab=as.character(labx),ylab=laby) ## 1
		axis(1, xaxp=c(corrx,5), main=titulo)
		axis(2,las=2)
#		arrows(datlinea[,1],(datlinea[,2]-(datlinea[,2]-datlinea[,3])), datlinea[,1],(datlinea[,2]+(datlinea[,4]-datlinea[,2])), length=0.1,angle=90, code=3,col=ccol[1]) ## 1
		lineashap<-seq(0,50,length=1000)
		ini<-as.list(ini)
		for (i in names(ini)){
			temp <- ini[[i]]
			storage.mode(temp) <- "double"
			assign(i, temp)
		}
   
   
   #Simple Logistic
   if(model==1){
			f <- function(x,c1,a,b){
				expr <- expression(c1/(1+exp(a+b*x)))
				eval(expr)
			}
			ylineash<-f(lineashap,c1,a,b)
                        coefi=NULL
		}
	
	
	#Logisitc 1 parameter
	if(model==2){
			f <- function(x,Dm){
				expr <- expression(1/(1+exp(-(x-Dm))))
				eval(expr)
			}
			ylineash<-f(lineashap,Dm)
                        coefi=NULL
		}
		
		
	#Logisitc 2 parameters
	if(model==3){
			f <- function(x,Dm,k){
				expr <- expression(1/(1+exp(-k*(x-Dm))))
				eval(expr)
			}
			ylineash<-f(lineashap,Dm,k)
                        coefi=NULL
		}
		
	#Logisitc 4 parameters
	if(model==4){
			f <- function(x,Dm,k,Yo,Yasym){
				expr <- expression(Yo + Yasym/(1+exp(-k*(x-Dm))))
				eval(expr)
			}
			ylineash<-f(lineashap,Dm,k,Yo,Yasym)
                        coefi=NULL
	}
		
	# Logistic  3 parameter
		if(model==5){
		  f <- function(x,c1,a,b){
		    expr <- expression(c1/(1+a*exp(b*x)))
		    eval(expr)
		  }
		  ylineash<-f(lineashap,c1,a,b)
		  coefi=NULL
		}
	
	
		
		lines(lineashap,ylineash,col=ccol[3],lwd=2) ##  3
		salidas<-list(ini=as.data.frame(ini),coefi=coefi)                                                  
		return(salidas)

  
}

#############################################
#############################################
#############################################
shape<-function(model,datashap,datao,ini,coefi){


	#Sigmoid or logistic
	if(model==1){
		if(length(datashap[,1])>(length(ini)+1)){
			x<-datashap[,1]
			y<-datashap[,2]}else{
			x<-datao[,1]
			y<-datao[,2]}
		ini<-as.list(ini)
		ind <- as.list(ini)
		for (i in names(ind)){
			temp <- ini[[i]]
			storage.mode(temp) <- "double"
			assign(i, temp)
		}
		f <- function(x,c1,a,b){
			expr <- expression(c1/(1+exp(a+b*x)))
			eval(expr)
		}
		j <- function(x,c1,a,b){
			expr <- expression(c1/(1+exp(a+b*x)))
			c(eval(D(expr, "c1")),eval(D(expr, "a")),eval(D(expr, "b")))
		}
		fcn <- function(ini, x, y, fcall, jcall)
			(y - do.call("fcall", c(list(x = x), as.list(ini))))
		out <- nls.lm(par = ini, fn = fcn,fcall = f, jcall = j,x = x, y = y,control  = nls.lm.control(maxiter = 500))
		stderro<-diag(ginv(out$hessian))
		estimate<-as.data.frame(out$par)
		return(list(estimados=estimate,f=f,stderro=stderro))
	}
	
		#Logisitc 1 parameter
	if(model==2){
		if(length(datashap[,1])>(length(ini)+1)){
			x<-datashap[,1]
			y<-datashap[,2]}else{
			x<-datao[,1]
			y<-datao[,2]}
		ini<-as.list(ini)
		ind <- as.list(ini)
		for (i in names(ind)){
			temp <- ini[[i]]
			storage.mode(temp) <- "double"
			assign(i, temp)
		}
		f <- function(x,Dm){
			expr <- expression(1/(1+exp(-(x-Dm))))
			eval(expr)
		}
		j <- function(x,Dm){
			expr <- expression(c1/(1+exp(a+b*x)))
			c(eval(D(expr, "Dm")))
		}
		fcn <- function(ini, x, y, fcall, jcall)
			(y - do.call("fcall", c(list(x = x), as.list(ini))))
		out <- nls.lm(par = ini, fn = fcn,fcall = f, jcall = j,x = x, y = y,control  = nls.lm.control(maxiter = 500))
		stderro<-diag(ginv(out$hessian))
		estimate<-as.data.frame(out$par)
		return(list(estimados=estimate,f=f,stderro=stderro))
	}
	
#Logisitc 2 parameters
	if(model==3){
		if(length(datashap[,1])>(length(ini)+1)){
			x<-datashap[,1]
			y<-datashap[,2]}else{
			x<-datao[,1]
			y<-datao[,2]}
		ini<-as.list(ini)
		ind <- as.list(ini)
		for (i in names(ind)){
			temp <- ini[[i]]
			storage.mode(temp) <- "double"
			assign(i, temp)
		}
		f <- function(x,Dm,k){
			expr <- expression(1/(1+exp(-k*(x-Dm))))
			eval(expr)
		}
		j <- function(x,Dm,k){
			expr <- expression(1/(1+exp(-k*(x-Dm))))
			c(eval(D(expr, "Dm")),eval(D(expr, "k")))
		}
		fcn <- function(ini, x, y, fcall, jcall)
			(y - do.call("fcall", c(list(x = x), as.list(ini))))
		out <- nls.lm(par = ini, fn = fcn,fcall = f, jcall = j,x = x, y = y,control  = nls.lm.control(maxiter = 500))
		stderro<-diag(ginv(out$hessian))
		estimate<-as.data.frame(out$par)
		return(list(estimados=estimate,f=f,stderro=stderro))
	}
	
	#Logisitc 4 parameters
	if(model==4){
		if(length(datashap[,1])>(length(ini)+1)){
			x<-datashap[,1]
			y<-datashap[,2]}else{
			x<-datao[,1]
			y<-datao[,2]}
		ini<-as.list(ini)
		ind <- as.list(ini)
		for (i in names(ind)){
			temp <- ini[[i]]
			storage.mode(temp) <- "double"
			assign(i, temp)
		}
		f <- function(x,Dm,k,Yo,Yasym){
			expr <- expression(Yo + Yasym/(1+exp(-k*(x-Dm))))
			eval(expr)
		}
		j <- function(x,Dm,k,Yo,Yasym){
			expr <- expression(Yo + Yasym/(1+exp(-k*(x-Dm))))
			c(eval(D(expr, "Dm")),eval(D(expr, "k")),eval(D(expr, "Yo")),eval(D(expr, "Yasym")))
		}
		fcn <- function(ini, x, y, fcall, jcall)
			(y - do.call("fcall", c(list(x = x), as.list(ini))))
		out <- nls.lm(par = ini, fn = fcn,fcall = f, jcall = j,x = x, y = y,control  = nls.lm.control(maxiter = 500))
		stderro<-diag(ginv(out$hessian))
		estimate<-as.data.frame(out$par)
		return(list(estimados=estimate,f=f,stderro=stderro))
	}
  
  
  #Logisitc 3 parameters
  if(model==5){
    if(length(datashap[,1])>(length(ini)+1)){
      x<-datashap[,1]
      y<-datashap[,2]}else{
        x<-datao[,1]
        y<-datao[,2]}
    ini<-as.list(ini)
    ind <- as.list(ini)
    for (i in names(ind)){
      temp <- ini[[i]]
      storage.mode(temp) <- "double"
      assign(i, temp)
    }
    f <- function(x,c1,a,b){
      expr <- expression(c1/(1+a*exp(b*x)))
      eval(expr)
    }
    j <- function(x,c1,a,b){
      expr <- expression(c1/(1+exp(a+b*x)))
      c(eval(D(expr, "c1")),eval(D(expr, "a")),eval(D(expr, "b")))
    }
    fcn <- function(ini, x, y, fcall, jcall)
      (y - do.call("fcall", c(list(x = x), as.list(ini))))
    out <- nls.lm(par = ini, fn = fcn,fcall = f, jcall = j,x = x, y = y,control  = nls.lm.control(maxiter = 500))
    stderro<-diag(ginv(out$hessian))
    estimate<-as.data.frame(out$par)
    return(list(estimados=estimate,f=f,stderro=stderro))
  }
  
	
	
}


#################################################
#################################################
#################################################
grafshape<-function(model,estshap,datashap,qtt,sdli,corrx=NULL,corry=NULL,mini,maxi,coefi,limit,tam,labx=NULL,laby=NULL, titulo=NULL,grises=FALSE, scaleY, scaleX,optDist){
nbY =5
k=1.25
	if(grises==TRUE){ccol=c("gray20","gray30")}else{ccol=c(4,2)}

	data<-datashap

	estshap<-as.list(estshap)

	finalshap<-estshap

	for (i in names(finalshap)) {

		temp <- finalshap[[i]]

		storage.mode(temp) <- "double"

		assign(i, temp)

	}

	xl<-seq(mini,maxi,length=300)
#	<-seq(mini,maxi,length=300)



	#Simple Logistic
	if(model==1) {
	
		expre<-expression(c1/(1+exp(a+b*xl)))
		yl<-eval(expre)
		
		optDist<- (log((c1/0.5)-1)- a)/b   #optimum Radus from the trap center

#		optDist<- (-a)/b   #optimum Radus from the trap
#	    optDist<- (log((estshap[1]/0.5)-1)- estshap[2])/estshap[3]

	}

	#Logisitc 1 parameter
	if(model==2){
	  expre<-expression(1/(1+exp(-(xl-Dm))))
	  yl<-eval(expre)
	  optDist<- (log((1/0.5)-1)- Dm)/-1   # optDist = Dm
	  
	  
	}

	#Logisitc 2 parameters
	if(model==3){
	  expre<-expression(1/(1+exp(-k*(xl-Dm))))
	  yl<-eval(expre)
	  optDist<- (log((1/0.5)-1)- k*Dm)/(-k) 
	  
	  
	}


	#Logisitc 4 parameters
	if(model==4){
	  expre<-expression(Yo + Yasym/(1+exp(-k*(xl-Dm))))
	  yl<-eval(expre)
	  optDist<- Dm - (1/k)*log((Yasym/(0.5-Yo))-1)
	  
	}
	
	
	#Logisic 4 parameter
	if(model==5) {
	  
	  expre<-expression(c1/(1+a*exp(b*xl)))
	  yl<-eval(expre)
	  optDist<- (log(((c1/0.5)-1)/a))/b   #optimum Radus from the trap
	  
	
	  
	}

	

	par(cex=tam)

	if(is.null(corrx)){corrx=c(min(data[,1],0),max(data[,1])*1.2);corrx2=seq(0,10*round(corrx[2]/10,1),5)}else{corrx2=seq(corrx[1],corrx[2],scaleX)} ## cambio
#	if(is.null(corrx)){corrx=c(min(data[,1],0),max(data[,1])*1.2);corrx2=seq(0,10*round(corrx[2]/10,1),5)}else{corrx2=seq(corrx[1],corrx[2],5)} ## cambio

	#if(is.null(corry)){corry=c(0,max(data[,2],yl)+max(data[,2])*0.2);corry2=seq(0,round(max(corry),1),0.1)}else{corry2=seq(corry[1],corry[2],scaleY)}              ## cambio
#if(is.null(corry)){corry=c(0,max(data[,2])*1.15);corry2=seq(0,max(corry),(corry[2]-corry[1])/nbY)}else{corry2=seq(corry[1],corry[2],scaleY)}              ## cambio
	
	if(is.null(corry)){corry=c(0,max(data[,2])*1.5);corry2=seq(0,max(corry),10)}else{corry2=seq(corry[1],corry[2],scaleY)}              ## cambio

	plot(data[,1],data[,2],frame=F,col=ccol[1],pch=19,xlim=corrx,ylim=corry,xlab=labx,ylab=laby,axes=F,xaxt = "n", main=titulo) ## 4
	n2= length(data[,1])
	#posic <- (1:7)[max(dataa[,2])==dataa[,2]][1] # excluye el ultimo valor para la regresion
		#dataaa <- dataa[1:posic,] # excluye el ultimo valor para la regresion

	posic=(1:n2)[max(data[,2])==data[,2]][1]
	modr= lm(data[1:posic,2]~data[1:posic,1])
#	if((1:length(estadios))[estadios==est]<length(estadios)-1){abline(modr,col=ccol[2],lty=2,lwd=2)}

	
#	abline(modr,col=ccol[2],lty=2,lwd=2)
	#axis(1, xaxp=c(corrx,5))

	#axis(2,las=2)

	axis(1, corrx2)  ## cambio

	axis(2, corry2,las=2) ## cambio
#	axis(side=2,round(seq(corry[1],k*corry[2],(k*corry[2]-corry[1])/nbY),5),las=2)



	linf<-yl+sdli*qtt

	lsup<-yl-sdli*qtt
	
#	linflec <-0*xl + 0.5 # useful to estimate the infection point


	if(limit=="yes"){

						
			
			lines(xl, yl, lwd=2,col=ccol[2]) ## 2

			lines(xl, linf, lwd=1,col=ccol[1],lty=2)

			lines(xl, lsup, lwd=1,col=ccol[1],lty=2)
			
#			lines(xl, linflec, lwd=1,col=ccol[1],lty=1)

		}

	if(limit=="no") lines(xl, yl, lwd=2,col=ccol[2]) ## 2 ..... solo aumente el tipo de modelo
	
	abline(v = optDist, h= 0.5, col = "red", lty = 2, lwd = 1)			#Projection vertical et horizontal du point optimum vers les axes.


#	arrows(data[,1],(data[,2]-(data[,2]-data[,3])), data[,1],(data[,2]+(data[,4]-data[,2])), length=0.1,angle=90, code=3,col=ccol[1]) ## 4

	
}

#############################################
#############################################
#############################################
# calculos de p-values cuando la hipotesis es bilateral

pvalores<-function(v,x,estchap){pval=pt(v,length(x)-length(estshap));if(v<0){pval=2*pval}else{pval=2*(1-pval)};return(pval)}


#############################################
#############################################
#############################################
stat_develop<-function(model,datashap,datao,estshap,coefi,stderro){
		ecua=c("Simple Logistic","Logisitc 1 parameter","Logisitc 2 parameters","Logisitc 4 parameters","Logisitc 3 parameters")                
				if(length(datashap[,1])>(length(estshap)+1)){
                                x<-datashap[,1]
                                y<-datashap[,2]
                    }else{
                                  x<-datao[,1]
                                  y<-datao[,2]
                    }
                ind <- as.list(estshap)
                for (i in names(ind))
                {
                                temp <- estshap[[i]]
                                storage.mode(temp) <- "double"
                                assign(i, temp)
                }

               
			   
	 #Simple Logistic
	if(model==1){
	  expre<-expression(c1/(1+exp(a+b*x)))
	  yl<-eval(expre)
	  gg<-"P(x) = c1/(1+exp(a+b.x))"
	  optDist<- (log((c1/0.5)-1)- a)/b 

	  
#	  optDist<- (- a)/b
#	  optDist<- (log((estshap[1]/0.5)-1)- estshap[2])/estshap[3]

	  
	}

	#Logisitc 1 parameter
	if(model==2){
	  expre<-expression(1/(1+exp(-(x-Dm))))
	  yl<-eval(expre)
	  gg<-"P(x) = 1/(1+exp(-(x-Dm)))"
	  optDist<- (log((1/0.5)-1)- Dm)/-1   # optDist = Dm
	}

	#Logisitc 2 parameters
	if(model==3){
	  expre<-expression(1/(1+exp(-k*(x-Dm))))
	  yl<-eval(expre)
	  gg<-"P(x) = 1/(1+exp(-k.(x-Dm)))"
	  optDist<- (log((1/0.5)-1)- k*Dm)/(-k) 
	}


	#Logisitc 4 parameters
	if(model==4){
	  expre<-expression(Yo + Yasym/(1+exp(-k*(x-Dm))))
	  yl<-eval(expre)
	  gg<-"P(x) = Yo + Yasym/(1+exp(-k.(x-Dm)))"
	  optDist<- Dm - (1/k)*log((Yasym/(0.5-Yo))-1)
	  
	    }
    
          
                
  #Logisitc 3 parameters
  if(model==5){
  expre<-expression(c1/(1+a*exp(b*x)))
  yl<-eval(expre)
  gg<-"P(x) = c1/(1+a.exp(b.x))"
  optDist<- (log(((c1/0.5)-1)/a))/b
  

   }            
                

			   
                art=0.00000000000000000000000000001
                sqe <- sum((y-yl)^2)+art
                df<-length(y)-length(estshap)
                sdli<-sqrt(sqe/df)
                qtt<-qt(0.025,df)
                var <- (sum((y-yl)^2)/(length(x) - length(estshap)))
                stderror<-sqrt(var*stderro)
                tvalues<-estshap/stderror;tvals2=data.frame(t(tvalues))
                tvalta<-qt(0.025,length(x) - length(estshap))
                #pvalues<-1-pt(as.numeric(tvalues),length(x) - length(estshap))
                pvalues<-apply(tvals2,1,pvalores,x,estchap)

                anva.1 <- c(length(estshap)-1,length(x)-length(estshap),length(x)-1)
                anva.2 <- c((sum(y^2)-length(y)*mean(y)^2)-sqe,sqe,sum(y^2)-length(y)*mean(y)^2)
                anva.3 <- c(anva.2[1]/anva.1[1],anva.2[2]/anva.1[2],NA)
                anva.4 <- c(anva.3[1]/anva.3[2],NA,NA)
                anva.5 <- c(1-pf(anva.4[1],anva.1[1],anva.1[2]),NA,NA)
                anva   <- cbind(anva.1,round(anva.2,4),round(anva.3,4),round(anva.4,4),round(anva.5,4))
                rownames(anva) <- c("Model","Error","Total")
                colnames(anva) <- c("DF","SS","MS","Fc","p")
                r<-1-sqe/sum((y-mean(y))^2)
                r_ajus<- 1 - ((length(x) - 1) / (length(x) - length(estshap))) * (1-r)
                #AC<-length(x)*log(sqe/length(x))+2*length(estshap) #Agregue /length(x) DM
#                AC<-length(x)*(log(2*pi*sqe/length(x))+1)+2*(length(estshap)+1) #Cambio segun Nonlinear Regression with R, pag 105. DM
                AC<-length(datashap[,1])*(log(2*pi*sqe/length(datashap[,1]))+1)+2*(length(estshap)+1) #Cambio segun Nonlinear Regression with R, pag 105. DM
                #n<-length(x); k<-length(estshap); if(n/k<40) {AC<-AC+2*k*(k+1)/(n-k-1); nombre="AICc"} else nombre="AIC" #Nuevo DM
                MSC<-log(sum((yl-mean(yl))^2)/sum((y-mean(y))^2))-2*length(estshap)/length(x)
				RMSE <- sqrt(mean((y-yl)^2)) #evaluation of the Root mean square
                #FRAMESEL<-data.frame(R2=round(r,3),R2_Adj=round(r_ajus,3),SSR=round(sqe,4),AIC=round(AC,3),MSC=round(MSC,3))
				FRAMESEL<-data.frame(R2=round(r,4),R2_Adj=round(r_ajus,4),SSR=round(sqe,4),AIC=round(AC,4),MSC=round(MSC,4),RMSE=round(RMSE,4))
                #colnames(FRAMESEL)[4]<-nombre #Nuevo DM
                rownames(FRAMESEL)<-c("")
                cat("\nNONLINEAR REGRESSION MODEL\n")
                cat("\nModel name:",ecua[model])
                cat("\nMethod:", "Marquardtr")
                cat("\nFormula: ", gg,"\n")
				        cat("\nAutodissemination\n")
				        cat("Trap Radius: R = ", optDist,"m\n")
				        cat("Traps Spacing: DTp =", optDist*sqrt(2),"m\n")
				        cat("Number Traps: NbT=",nbrTrapPerHa(optDist) ,"Traps/Ha \n")
                cat("\n")
                cat("Parameters\n")
            
                
                estshap2<-data.frame(matrix(round(estshap,4)),round(stderror,5),formatC(round(as.numeric(tvalues),5)),pvalues)
                                colnames(estshap2)<-c("Estimate","Std.Error","t value","Pr(>|t|)")
                                rownames(estshap2)<-c(colnames(estshap))
                                para<-t(matrix(paste(round(estshap,2),"(","?",formatC(stderror,5),")")))
                                colnames(para)<-names(estshap)

               param<-data.frame(Model=gg,para,d.f.=paste(anva.1[1],",",anva.1[2]),F=round(anva.4[1],3),P.value=round(anva.5[1],3),FRAMESEL)
                
				print(estshap2)
                

                cat("\n")
                cat("Analysis of variance\n")
                print(anva,na.print = "")
                cat("\nSelection criteria")
                print(t(FRAMESEL))
                if ( length(estshap)!=length(stderror) ) { stderror<-c(rep(NA,(length(estshap)-length(stderror))),stderror)}  ###Agregue DM
                names(stderror)<-names(estshap) #Agrege DM
               
                salidas<-list(ecuacion=gg,parmer=estshap,frames= FRAMESEL,q=qtt,sdli=sdli,param=param,ecuaci=formula(paste("y~",as.character(expre))),Std.Error=stderror,optDist=optDist) # Agregue "Std.Error=stderror" DM
                
                return(salidas)
}

################################
# Re
################################




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
