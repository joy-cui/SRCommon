/*    */ package com.serenegiant.utils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GPS
/*    */ {
/*    */   public static class Datum
/*    */   {
/*    */     public final double a;
/*    */     public final double b;
/*    */     public final double inv_ellipticity;
/*    */     public final double e2;
/*    */     public final double ae2;
/*    */     
/*    */     public Datum(double a, double b, double ellipticity, boolean inverse) {
/* 30 */       this.a = a;
/* 31 */       this.b = b;
/* 32 */       this.inv_ellipticity = inverse ? ellipticity : (1.0D / ellipticity);
/* 33 */       double a2 = a * a;
/* 34 */       this.e2 = (a2 - b * b) / a2;
/* 35 */       this.ae2 = a * (1.0D - this.e2);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public static final Datum BESSEL = new Datum(6377397.155D, 6356079.0D, 299.152813D, true);
/* 47 */   public static final Datum WGS84 = new Datum(6378137.0D, 6356752.314245D, 298.257223563D, true);
/* 48 */   public static final Datum GRS80 = new Datum(6378137.0D, 6356752.31414D, 298.257222101D, true);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static double distance(Datum datum, double latitude_degree1, double longitude_degree1, double altitude1, double latitude_degree2, double longitude_degree2, double altitude2) {
/* 66 */     double latitude1 = Math.toRadians(latitude_degree1);
/* 67 */     double latitude2 = Math.toRadians(latitude_degree2);
/* 68 */     double longitude1 = Math.toRadians(longitude_degree1);
/* 69 */     double longitude2 = Math.toRadians(longitude_degree2);
/*    */     
/* 71 */     double dy = latitude1 - latitude2;
/* 72 */     double dx = longitude1 - longitude2;
/*    */     
/* 74 */     double u = (latitude1 + latitude2) / 2.0D;
/* 75 */     double sin_u = Math.sin(u);
/* 76 */     double w = Math.sqrt(1.0D - datum.e2 * sin_u * sin_u);
/* 77 */     double m = datum.ae2 / w * w * w;
/* 78 */     double n = datum.a / w;
/*    */     
/* 80 */     double dm = dy * m;
/* 81 */     double dn = dx * n * Math.cos(u);
/* 82 */     return Math.sqrt(dm * dm + dn * dn);
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\GPS.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */