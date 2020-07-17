/*     */ package com.serenegiant.math;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ public abstract class BaseBounds
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 5504958491886331189L;
/*  10 */   public final Vector position = new Vector();
/*  11 */   public final Vector angle = new Vector();
/*     */   
/*     */   public float radius;
/*     */ 
/*     */   
/*     */   public BaseBounds() {}
/*     */   
/*  18 */   public BaseBounds(BaseBounds src) { set(src); }
/*     */ 
/*     */   
/*     */   public BaseBounds(float center_x, float center_y, float radius) {
/*  22 */     this.position.set(center_x, center_y);
/*  23 */     this.radius = radius;
/*     */   }
/*     */   
/*     */   public BaseBounds(float center_x, float center_y, float center_z, float radius) {
/*  27 */     this.position.set(center_x, center_y, center_z);
/*  28 */     this.radius = radius;
/*     */   }
/*     */   
/*     */   public BaseBounds set(BaseBounds src) {
/*  32 */     this.position.set(src.position);
/*  33 */     this.angle.set(src.angle);
/*  34 */     this.radius = src.radius;
/*  35 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  40 */   protected boolean ptInBoundsSphere(float x, float y, float z, float r) { return (this.position.distSquared(x, y, z) < r * r); }
/*     */ 
/*     */ 
/*     */   
/*  44 */   public boolean ptInBounds(float x, float y) { return ptInBounds(x, y, this.position.z); }
/*     */ 
/*     */ 
/*     */   
/*  48 */   public boolean ptInBounds(Vector other) { return ptInBounds(other.x, other.y, other.z); }
/*     */ 
/*     */   
/*     */   public abstract boolean ptInBounds(float paramFloat1, float paramFloat2, float paramFloat3);
/*     */   
/*     */   public BaseBounds move(float offset_x, float offset_y) {
/*  54 */     this.position.add(offset_x, offset_y);
/*  55 */     return this;
/*     */   }
/*     */   
/*     */   public BaseBounds move(float offset_x, float offset_y, float offset_z) {
/*  59 */     this.position.add(offset_x, offset_y, offset_z);
/*  60 */     return this;
/*     */   }
/*     */   
/*     */   public BaseBounds move(Vector offset) {
/*  64 */     this.position.add(offset);
/*  65 */     return this;
/*     */   }
/*     */   
/*     */   public BaseBounds setPosition(Vector pos) {
/*  69 */     this.position.set(pos);
/*  70 */     return this;
/*     */   }
/*     */   
/*     */   public BaseBounds setPosition(float x, float y) {
/*  74 */     this.position.set(x, y);
/*  75 */     return this;
/*     */   }
/*     */   
/*     */   public BaseBounds setPosition(float x, float y, float z) {
/*  79 */     this.position.set(x, y, z);
/*  80 */     return this;
/*     */   }
/*     */ 
/*     */   
/*  84 */   public void centerX(float x) { this.position.x = x; }
/*     */ 
/*     */ 
/*     */   
/*  88 */   public float centerX() { return this.position.x; }
/*     */ 
/*     */ 
/*     */   
/*  92 */   public void centerY(float y) { this.position.y = y; }
/*     */ 
/*     */ 
/*     */   
/*  96 */   public float centerY() { return this.position.y; }
/*     */ 
/*     */ 
/*     */   
/* 100 */   public void centerZ(float z) { this.position.z = z; }
/*     */ 
/*     */ 
/*     */   
/* 104 */   public float centerZ() { return this.position.z; }
/*     */ 
/*     */ 
/*     */   
/* 108 */   public void rotate(Vector angle) { angle.set(angle.x, angle.y, angle.z); }
/*     */ 
/*     */ 
/*     */   
/* 112 */   public void rotate(float x, float y, float z) { this.angle.set(x, y, z); }
/*     */ 
/*     */ 
/*     */   
/* 116 */   public void rotateX(float angle) { this.angle.x = angle; }
/*     */ 
/*     */ 
/*     */   
/* 120 */   public void rotateY(float angle) { this.angle.y = angle; }
/*     */ 
/*     */ 
/*     */   
/* 124 */   public void rotateZ(float angle) { this.angle.z = angle; }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\math\BaseBounds.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */