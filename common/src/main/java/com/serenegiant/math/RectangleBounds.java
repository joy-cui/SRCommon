/*     */ package com.serenegiant.math;
/*     */ 
/*     */ import android.graphics.Rect;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RectangleBounds
/*     */   extends BaseBounds
/*     */ {
/*     */   private static final long serialVersionUID = 260429282595037220L;
/*  11 */   public final Vector box = new Vector();
/*  12 */   private final Rect boundsRect = new Rect();
/*  13 */   private final Vector w = new Vector();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RectangleBounds(float center_x, float center_y, float center_z, float width, float height, float depth) {
/*  25 */     this.position.set(center_x, center_y, center_z);
/*  26 */     this.box.set(width / 2.0F, height / 2.0F, depth / 2.0F);
/*  27 */     this.radius = this.box.len();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   public RectangleBounds(float center_x, float center_y, float width, float height) { this(center_x, center_y, 0.0F, width, height, 0.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   public RectangleBounds(Vector center, float width, float height) { this(center.x, center.y, center.z, width, height, 0.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RectangleBounds(Vector lowerLeft, Vector upperRight) {
/*  58 */     if (lowerLeft.x > upperRight.x) {
/*  59 */       float a = lowerLeft.x; lowerLeft.x = upperRight.x; upperRight.x = a;
/*     */     } 
/*  61 */     if (lowerLeft.y > upperRight.y) {
/*  62 */       float a = lowerLeft.y; lowerLeft.y = upperRight.y; upperRight.y = a;
/*     */     } 
/*  64 */     if (lowerLeft.z > upperRight.z) {
/*  65 */       float a = lowerLeft.z; lowerLeft.z = upperRight.z; upperRight.z = a;
/*     */     } 
/*  67 */     setPosition((upperRight.x - lowerLeft.x) / 2.0F, (upperRight.y - lowerLeft.y) / 2.0F, (upperRight.z - lowerLeft.z) / 2.0F);
/*     */ 
/*     */ 
/*     */     
/*  71 */     this.box.set(this.position).sub(lowerLeft);
/*  72 */     this.radius = this.box.len();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   public RectangleBounds(Rect rect) { this(rect.centerX(), rect.centerY(), rect.width(), rect.height()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean ptInBounds(float x, float y, float z) {
/*  89 */     boolean f = ptInBoundsSphere(x, y, z, this.radius);
/*  90 */     if (f) {
/*     */ 
/*     */       
/*  93 */       this.w.set(x, y, z).sub(this.position).rotate(this.angle, -1.0F);
/*     */       
/*  95 */       float x1 = this.position.x - this.box.x;
/*  96 */       float x2 = this.position.x + this.box.x;
/*  97 */       float y1 = this.position.y - this.box.y;
/*  98 */       float y2 = this.position.y + this.box.y;
/*  99 */       float z1 = this.position.z - this.box.z;
/* 100 */       float z2 = this.position.z + this.box.z;
/* 101 */       f = (this.w.x >= x1 && this.w.x <= x2 && this.w.y >= y1 && this.w.y <= y2 && this.w.z >= z1 && this.w.z <= z2);
/*     */     } 
/*     */ 
/*     */     
/* 105 */     return f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rect boundsRect() {
/* 113 */     this.boundsRect.set((int)(this.position.x - this.box.x), (int)(this.position.y - this.box.y), (int)(this.position.x + this.box.x), (int)(this.position.y + this.box.y));
/*     */ 
/*     */     
/* 116 */     return this.boundsRect;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rect boundsRect(float a) {
/* 125 */     this.boundsRect.set((int)(this.position.x - this.box.x * a), (int)(this.position.y - this.box.y * a), (int)(this.position.x + this.box.x * a), (int)(this.position.y + this.box.y * a));
/*     */ 
/*     */     
/* 128 */     return this.boundsRect;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\math\RectangleBounds.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */