/*    */ package com.serenegiant.media;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TimeoutException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -7207769104864850593L;
/*    */   
/*    */   public TimeoutException() {}
/*    */   
/* 32 */   public TimeoutException(String detailMessage) { super(detailMessage); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public TimeoutException(Throwable throwable) { super(throwable); }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public TimeoutException(String detailMessage, Throwable throwable) { super(detailMessage, throwable); }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\TimeoutException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */