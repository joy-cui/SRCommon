/*     */ package com.serenegiant.math;
/*     */ 
/*     */ public class OverlapTester
/*     */ {
/*     */   public static boolean check(BaseBounds b1, BaseBounds b2) {
/*   6 */     float distance = b1.position.distSquared(b2.position);
/*   7 */     float radiusSum = b1.radius + b2.radius;
/*   8 */     return (distance <= radiusSum * radiusSum);
/*     */   }
/*     */   
/*     */   public static boolean check(CircleBounds c1, CircleBounds c2) {
/*  12 */     float distance = c1.position.distSquared(c2.position);
/*  13 */     float radiusSum = c1.radius + c2.radius;
/*  14 */     return (distance <= radiusSum * radiusSum);
/*     */   }
/*     */   
/*  17 */   private static final Vector r1L = new Vector();
/*  18 */   private static final Vector r2L = new Vector();
/*     */   public static boolean check(RectangleBounds r1, RectangleBounds r2) {
/*  20 */     r1L.set(r1.position).sub(r1.box);
/*  21 */     float r1x = r1.box.x * 2.0F;
/*  22 */     float r1y = r1.box.y * 2.0F;
/*  23 */     float r1z = r1.box.z * 2.0F;
/*  24 */     r2L.set(r2.position).sub(r2.box);
/*  25 */     float r2x = r2.box.x * 2.0F;
/*  26 */     float r2y = r2.box.y * 2.0F;
/*  27 */     float r2z = r2.box.z * 2.0F;
/*     */     
/*  29 */     return (r1L.x < r2L.x + r2x && r1L.x + r1x > r2L.x && r1L.y < r2L.y + r2y && r1L.y + r1y > r2L.y && r1L.z < r2L.z + r2z && r1L.z + r1z > r2L.z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean check(CircleBounds c, RectangleBounds r) {
/*  38 */     float cx = c.position.x;
/*  39 */     float cy = c.position.y;
/*  40 */     float cz = c.position.z;
/*  41 */     r1L.set(r.position).sub(r.box);
/*  42 */     float rx = r.box.x * 2.0F;
/*  43 */     float ry = r.box.y * 2.0F;
/*  44 */     float rz = r.box.z * 2.0F;
/*     */     
/*  46 */     if (c.position.x < r1L.x) {
/*  47 */       cx = r1L.x;
/*  48 */     } else if (c.position.x > r1L.x + rx) {
/*  49 */       cx = r1L.x + rx;
/*     */     } 
/*     */     
/*  52 */     if (c.position.y < r1L.y) {
/*  53 */       cy = r1L.y;
/*  54 */     } else if (c.position.y > r1L.y + ry) {
/*  55 */       cy = r1L.y + ry;
/*     */     } 
/*     */     
/*  58 */     if (c.position.z < r1L.z) {
/*  59 */       cz = r1L.z;
/*  60 */     } else if (c.position.z > r1L.z + rz) {
/*  61 */       cz = r1L.z + rz;
/*     */     } 
/*     */     
/*  64 */     return (c.position.distSquared(cx, cy, cz) < c.radius * c.radius);
/*     */   }
/*     */ 
/*     */   
/*  68 */   public static boolean check(CircleBounds c, Vector p) { return (c.position.distSquared(p) < c.radius * c.radius); }
/*     */ 
/*     */ 
/*     */   
/*  72 */   public static boolean check(CircleBounds c, float x, float y, float z) { return (c.position.distSquared(x, y, z) < c.radius * c.radius); }
/*     */ 
/*     */ 
/*     */   
/*  76 */   public static boolean check(CircleBounds c, float x, float y) { return (c.position.distSquared(x, y) < c.radius * c.radius); }
/*     */ 
/*     */ 
/*     */   
/*  80 */   public static boolean check(RectangleBounds r, Vector p) { return check(r, p.x, p.y, p.z); }
/*     */ 
/*     */ 
/*     */   
/*  84 */   public static boolean check(RectangleBounds r, float x, float y) { return check(r, x, y, 0.0F); }
/*     */ 
/*     */   
/*     */   public static boolean check(RectangleBounds r, float x, float y, float z) {
/*  88 */     r1L.set(r.position).sub(r.box);
/*  89 */     return (r1L.x <= x && r1L.x + r.box.x * 2.0F >= x && r1L.y <= y && r1L.y + r.box.y * 2.0F >= y && r1L.z <= z && r1L.z + r.box.z * 2.0F >= z);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean check(SphereBounds s1, SphereBounds s2) {
/*  95 */     float distance = s1.position.distance(s2.position);
/*  96 */     return (distance <= s1.radius + s2.radius);
/*     */   }
/*     */ 
/*     */   
/* 100 */   public static boolean check(SphereBounds s, Vector pos) { return (s.position.distSquared(pos) < s.radius * s.radius); }
/*     */ 
/*     */ 
/*     */   
/* 104 */   public static boolean check(SphereBounds s, float x, float y, float z) { return (s.position.distance(x, y, z) < s.radius * s.radius); }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\math\OverlapTester.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */