library(MASS)
library(sp)
library(maptools)
library(rgdal)
library(maps)
library(doRNG)


#This function divided the area of study caracterized by "ilat" in R part.

p.area<-function(ilat,R)
{
####################################################################################################
# ilat  is a vector of length 2 of the of the extreme values of latitude.
#
##################################################################################################
  R=R+1
  lats=seq(ilat[1],ilat[2],length.out=R)+0.0000000001
  mat.lat=matrix(NA,R-1,2)
  for(i in 2:R) 
  {
    mat.lat[i-1,]=c(lats[i-1],lats[i])
  }
  return(mat.lat)
}


#########################################################


#Indices joining of separate areas according to latitude
#########################################################
zone.div<-function(dir1,dir2,ilon,ilat,R,dir.out,name.out=name.out,model,filtro,gg = NULL,coefEstimated = NULL,alg)
{
  
  lats=p.area(ilat,R)
  regRemoved = NULL
  #############################################
  # Running through each area and store your data
  #############################################
  
  for(j in R:1){
    
    TempsAll=GenMatriz(dir1,dir2,ilon,lats[j,])
    
    coords=TempsAll$coords;nfil=nrow(coords)
	if(nfil == 0)
	{ 
		regRemoved = c(regRemoved,j)
		next
	}	
    
    x1=TempsAll$x1
    
    y1=TempsAll$y1
    
    
    RFinal=matrix(NA,nfil,1) ## change the dimension
    
    ################################
    
    # Corriendo por punto del Area j
	#Area running point j
    
    ################################
    
    #system.time(
    
    for(i in 1:nfil){
       
      RFinal[i,]= GenActIndex(i,TempsAll=TempsAll,coords=coords,x1=x1,y1=y1,model,filtro,gg,coefEstimated,alg)$indices 
    }
    
    #)
    
    rm(TempsAll);rm(x1);rm(y1)
    
    Inds=data.frame(coords[1:nfil,],RFinal)
    
    rm(RFinal)
    
    ##################################################################
    
    # Generating header files: Lon - Lat - GI - AI - ERI
    
    ##################################################################
    
    write.table(Inds,paste(dir.out,"file",j,".txt",sep=""),row.names = F)
    
    rm(Inds)
  }
  
  ###########################################
  
  # Running through each area and joining their data
  
 
#In this part before trying to read the file for the result we insure that it has been create.
	Inds = NULL

	for(j in R:1)
	{
	  print(j)
	  if (!(j %in% regRemoved))
	  {
		TempInds=read.table(paste(dir.out,"file",j,".txt",sep=""),header=T)
		Inds=rbind(Inds,TempInds)
	  }
	  
	}

  
   rm(TempInds)
  gridded(Inds) = ~x+y ## Creating the Object Grid
  
  writeAsciiGrid(Inds["RFinal"], na.value = -9999,paste(dir.out,"IMT.asc",sep="")) 
  
	#IMT = readAsciiGrid(paste(dir.out,"IMT.asc",sep=""))
	#spplot(IMT[c("IMT.asc")])

}

#############################################
#
# function generation the index
########################################

GenActIndex<-function(posic,TempsAll=TempsAll,coords=coords,x1=x1,y1=y1,model,filtro, gg = NULL, coefEstimated = NULL,alg)
{
#######################################################################################
#  coords=coords(a set of coordinate),x1=x1( longitude of the giving coordinate),y1=y1(latitude of using coordinate),
#  out(model to use) ,filtro(a vector of length 2 will be used to filter inpuut value of input variable of the function)
#
##########################################################################################

  d1<-1:length(x1);d2<-1:length(y1);filtroin=TRUE
  plon=d1[x1==coords[posic,1]]
  plat=d2[y1==coords[posic,2]]
  
  Table=data.frame(mini=c(TempsAll$zTmin1[plon,plat],TempsAll$zTmin2[plon,plat],TempsAll$zTmin3[plon,plat],TempsAll$zTmin4[plon,plat],TempsAll$zTmin5[plon,plat],TempsAll$zTmin6[plon,plat],TempsAll$zTmin7[plon,plat],TempsAll$zTmin8[plon,plat],TempsAll$zTmin9[plon,plat],TempsAll$zTmin10[plon,plat],TempsAll$zTmin11[plon,plat],TempsAll$zTmin12[plon,plat]),
                   maxi=c(TempsAll$zTmax1[plon,plat],TempsAll$zTmax2[plon,plat],TempsAll$zTmax3[plon,plat],TempsAll$zTmax4[plon,plat],TempsAll$zTmax5[plon,plat],TempsAll$zTmax6[plon,plat],TempsAll$zTmax7[plon,plat],TempsAll$zTmax8[plon,plat],TempsAll$zTmax9[plon,plat],TempsAll$zTmax10[plon,plat],TempsAll$zTmax11[plon,plat],TempsAll$zTmax12[plon,plat]))
  
  Table=Table/10;Table2=cbind(id=1:nrow(Table),Table) ## filter at this temperature
 
  
  if(length(Table[is.na(Table)])==0)
  {
    if(!is.null(filtro))
	{
		tmm=apply(Table,2,mean,na.rm=TRUE);
		if(tmm[1] > filtro[1] && tmm[2] < filtro[2]){filtroin=TRUE}else{filtroin=FALSE}
	}
    if(filtroin)
    {
      
    
    
      IME=mean(tmm)  # we compute the mean value using the min and the max temperature for the giving coordinate.
      
	 if (alg == "Newton")
	  {IMT = predict(model, list(x = IME))}  #we apply the model to the mean of extracted temperature for each location to compute the value of mortality for the giving location in case of newton algo
	  
	  if(alg == "Marquardtr") 
	{
		x = IME
		
		ind <- as.list(coefEstimated)
		  for (i in names(ind))
		  {
			temp <- coefEstimated[[i]]
			storage.mode(temp) <- "double"
			assign(i, temp)
			
		  }
		
		forex <- gg[[length(gg)]]  # #we apply the model to the mean of extracted temperature for each location to compute the value of mortality for the giving location  in case of Marquardtr algo.
		ylexpx<-as.expression(forex)
		IMT<-eval(ylexpx)
	
    
    }
	  
   

      indices=c(IMT=IMT)
      return(list(indices=indices))
    }else{indices=c(IMT=NA);return(list(indices=indices))}
  }else{
    
    indices=c(IMT=NA)
    return(list(indices=indices))
  }
  
}
##########################################################
# This function read temperature data and construct the matrix of location will be used to 
# extract temperature data .
##########################################################
GenMatriz<-function(dir1,dir2,ilon,ilat){
  archivos1=list.files(dir1,pattern="flt");archivos1=paste(dir1,"/",archivos1,sep="")
  archivos2=list.files(dir2,pattern="flt");archivos2=paste(dir2,"/",archivos2,sep="")
  
  Tmin1=readGDAL(archivos1[1])
  
  # for the extraction of data use the function:
  
  geodat=data.frame(coordinates(Tmin1))
  
  ###########################################
  ## Extraction of longitudes and latitudes
  
  x <- c(geodat[,1]);x <- unique(x)
  n1=length(x) ## tama�o
  
  y <- c(geodat[,2]); y <- unique(y)
  n2=length(y)
  
  #rm(geodat)
  
  #######################################################################
  ## Extraction of resolutions for both length to latitude
  
  v1=Tmin1@grid@cellsize[1]/2
  v2=Tmin1@grid@cellsize[2]/2
  
  
  ##############################################################
  ## Defining ranges in the longitude and latitude with position
  
  r11=ilon[1];r12=ilon[2];r21=ilat[1];r22=ilat[2]
  
  k1=rownames(geodat[geodat[,1] >= (r11-v1) & geodat[,1] <= (r12+v1),])
  k2=rownames(geodat[geodat[,2] >= (r21-v2) & geodat[,2] <= (r22+v2),])
  
  sector=intersect(k1,k2)
  sector=as.numeric(sector)
  
  coords=geodat[sector,]
  
  rm(geodat)
  
  
  #################################################
  ## Defining ranges in the longitude and latitude 
  
  ind1<-1:n1
  d1=x>=(r11-v1) & x<=(r12+v1)
  x1=x[d1]
  ind1=ind1[d1]
  
  
  ind2<-1:n2
  d2=y>=(r21-v2) & y<=(r22+v2)
  y1=y[d2]
  ind2=ind2[d2]
  
  
  ####################################################################################
  ## Creating the array containing the values of the variable for each coordinate
  
  z <- c(Tmin1@data[,1]);zTmin1=matrix(z,n1,n2);rm(Tmin1)
  zTmin1=zTmin1[ind1,ind2]; rownames(zTmin1)=x1; colnames(zTmin1)=y1
  Tmin2=readGDAL(archivos1[2])
  z <- c(Tmin2@data[,1]);zTmin2=matrix(z,n1,n2);rm(Tmin2)
  zTmin2=zTmin2[ind1,ind2]; rownames(zTmin2)=x1; colnames(zTmin2)=y1
  Tmin3=readGDAL(archivos1[3])
  z <- c(Tmin3@data[,1]);zTmin3=matrix(z,n1,n2);rm(Tmin3)
  zTmin3=zTmin3[ind1,ind2]; rownames(zTmin3)=x1; colnames(zTmin3)=y1
  Tmin4=readGDAL(archivos1[4])
  z <- c(Tmin4@data[,1]);zTmin4=matrix(z,n1,n2);rm(Tmin4)
  zTmin4=zTmin4[ind1,ind2]; rownames(zTmin4)=x1; colnames(zTmin4)=y1
  Tmin5=readGDAL(archivos1[5])
  z <- c(Tmin5@data[,1]);zTmin5=matrix(z,n1,n2);rm(Tmin5)
  zTmin5=zTmin5[ind1,ind2]; rownames(zTmin5)=x1; colnames(zTmin5)=y1
  Tmin6=readGDAL(archivos1[6])
  z <- c(Tmin6@data[,1]);zTmin6=matrix(z,n1,n2);rm(Tmin6)
  zTmin6=zTmin6[ind1,ind2]; rownames(zTmin6)=x1; colnames(zTmin6)=y1
  Tmin7=readGDAL(archivos1[7])
  z <- c(Tmin7@data[,1]);zTmin7=matrix(z,n1,n2);rm(Tmin7)
  zTmin7=zTmin7[ind1,ind2]; rownames(zTmin7)=x1; colnames(zTmin7)=y1
  Tmin8=readGDAL(archivos1[8])
  z <- c(Tmin8@data[,1]);zTmin8=matrix(z,n1,n2);rm(Tmin8)
  zTmin8=zTmin8[ind1,ind2]; rownames(zTmin8)=x1; colnames(zTmin8)=y1
  Tmin9=readGDAL(archivos1[9])
  z <- c(Tmin9@data[,1]);zTmin9=matrix(z,n1,n2);rm(Tmin9)
  zTmin9=zTmin9[ind1,ind2]; rownames(zTmin9)=x1; colnames(zTmin9)=y1
  Tmin10=readGDAL(archivos1[10])
  z <- c(Tmin10@data[,1]);zTmin10=matrix(z,n1,n2);rm(Tmin10)
  zTmin10=zTmin10[ind1,ind2]; rownames(zTmin10)=x1; colnames(zTmin10)=y1
  Tmin11=readGDAL(archivos1[11])
  z <- c(Tmin11@data[,1]);zTmin11=matrix(z,n1,n2);rm(Tmin11)
  zTmin11=zTmin11[ind1,ind2]; rownames(zTmin11)=x1; colnames(zTmin11)=y1
  Tmin12=readGDAL(archivos1[12])
  z <- c(Tmin12@data[,1]);zTmin12=matrix(z,n1,n2);rm(Tmin12)
  zTmin12=zTmin12[ind1,ind2]; rownames(zTmin12)=x1; colnames(zTmin12)=y1
  rm(z)
  
  Tmax1=readGDAL(archivos2[1])
  z <- c(Tmax1@data[,1]);zTmax1=matrix(z,n1,n2);rm(Tmax1)
  zTmax1=zTmax1[ind1,ind2]; rownames(zTmax1)=x1; colnames(zTmax1)=y1
  Tmax2=readGDAL(archivos2[2])
  z <- c(Tmax2@data[,1]);zTmax2=matrix(z,n1,n2);rm(Tmax2)
  zTmax2=zTmax2[ind1,ind2]; rownames(zTmax2)=x1; colnames(zTmax2)=y1
  Tmax3=readGDAL(archivos2[3])
  z <- c(Tmax3@data[,1]);zTmax3=matrix(z,n1,n2);rm(Tmax3)
  zTmax3=zTmax3[ind1,ind2]; rownames(zTmax3)=x1; colnames(zTmax3)=y1
  Tmax4=readGDAL(archivos2[4])
  z <- c(Tmax4@data[,1]);zTmax4=matrix(z,n1,n2);rm(Tmax4)
  zTmax4=zTmax4[ind1,ind2]; rownames(zTmax4)=x1; colnames(zTmax4)=y1
  Tmax5=readGDAL(archivos2[5])
  z <- c(Tmax5@data[,1]);zTmax5=matrix(z,n1,n2);rm(Tmax5)
  zTmax5=zTmax5[ind1,ind2]; rownames(zTmax5)=x1; colnames(zTmax5)=y1
  Tmax6=readGDAL(archivos2[6])
  z <- c(Tmax6@data[,1]);zTmax6=matrix(z,n1,n2);rm(Tmax6)
  zTmax6=zTmax6[ind1,ind2]; rownames(zTmax6)=x1; colnames(zTmax6)=y1
  Tmax7=readGDAL(archivos2[7])
  z <- c(Tmax7@data[,1]);zTmax7=matrix(z,n1,n2);rm(Tmax7)
  zTmax7=zTmax7[ind1,ind2]; rownames(zTmax7)=x1; colnames(zTmax7)=y1
  Tmax8=readGDAL(archivos2[8])
  z <- c(Tmax8@data[,1]);zTmax8=matrix(z,n1,n2);rm(Tmax8)
  zTmax8=zTmax8[ind1,ind2]; rownames(zTmax8)=x1; colnames(zTmax8)=y1
  Tmax9=readGDAL(archivos2[9])
  z <- c(Tmax9@data[,1]);zTmax9=matrix(z,n1,n2);rm(Tmax9)
  zTmax9=zTmax9[ind1,ind2]; rownames(zTmax9)=x1; colnames(zTmax9)=y1
  Tmax10=readGDAL(archivos2[10])
  z <- c(Tmax10@data[,1]);zTmax10=matrix(z,n1,n2);rm(Tmax10)
  zTmax10=zTmax10[ind1,ind2]; rownames(zTmax10)=x1; colnames(zTmax10)=y1
  Tmax11=readGDAL(archivos2[11])
  z <- c(Tmax11@data[,1]);zTmax11=matrix(z,n1,n2);rm(Tmax11)
  zTmax11=zTmax11[ind1,ind2]; rownames(zTmax11)=x1; colnames(zTmax11)=y1
  Tmax12=readGDAL(archivos2[12])
  z <- c(Tmax12@data[,1]);zTmax12=matrix(z,n1,n2);rm(Tmax12)
  zTmax12=zTmax12[ind1,ind2]; rownames(zTmax12)=x1; colnames(zTmax12)=y1
  rm(z)
  
  return(list(zTmin1=zTmin1,zTmin2=zTmin2,zTmin3=zTmin3,zTmin4=zTmin4,zTmin5=zTmin5,zTmin6=zTmin6,zTmin7=zTmin7,zTmin8=zTmin8,zTmin9=zTmin9,zTmin10=zTmin10,zTmin11=zTmin11,zTmin12=zTmin12,zTmax1=zTmax1,zTmax2=zTmax2,zTmax3=zTmax3,zTmax4=zTmax4,zTmax5=zTmax5,zTmax6=zTmax6,zTmax7=zTmax7,zTmax8=zTmax8,zTmax9=zTmax9,zTmax10=zTmax10,zTmax11=zTmax11,zTmax12=zTmax12,coords=coords,x1=x1,y1=y1))
}


plotMap<- function(dir.out,fileName)
{
	IMT = readAsciiGrid(paste(dir.out,fileName,".asc",sep=""))
	print(spplot(IMT[c(paste(fileName,".asc",sep=""))]))

		
}
