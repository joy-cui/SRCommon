/*    */ package com.serenegiant.graphics;
/*    */ 
/*    */ import android.graphics.Canvas;
/*    */ import android.graphics.Paint;
/*    */ import android.graphics.Path;
/*    */ import android.graphics.drawable.shapes.Shape;
/*    */ 
/*    */ 
/*    */ public class PathShape
/*    */   extends BaseShape
/*    */ {
/* 12 */   private Path mPath = new Path();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PathShape(Path path, float stdWidth, float stdHeight) {
/* 23 */     super(stdWidth, stdHeight);
/* 24 */     setPath(path);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 29 */   protected void doDraw(Canvas canvas, Paint paint) { canvas.drawPath(this.mPath, paint); }
/*    */ 
/*    */ 
/*    */   
/*    */   public PathShape clone() throws CloneNotSupportedException {
/* 34 */     PathShape shape = (PathShape)super.clone();
/* 35 */     shape.mPath = new Path(this.mPath);
/* 36 */     return shape;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPath(Path path) {
/* 44 */     this.mPath.reset();
/* 45 */     if (path != null && !path.isEmpty()) {
/* 46 */       this.mPath.addPath(path);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public Path getPath() { return this.mPath; }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\graphics\PathShape.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */