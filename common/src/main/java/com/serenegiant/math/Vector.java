/*     */ package com.serenegiant.math;
/*     */ 
/*     */ import android.opengl.Matrix;
/*     */ import java.io.Serializable;
/*     */ import java.util.Locale;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Vector
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = 1620440892067002860L;
/*     */   public static final float TO_RADIAN = 0.017453292F;
/*     */   public static final float TO_DEGREE = 57.29578F;
/*  35 */   public static final Vector zeroVector = new Vector();
/*  36 */   public static final Vector normVector = new Vector(1.0F, 1.0F, 1.0F);
/*     */   
/*  38 */   private static final float[] matrix = new float[16];
/*  39 */   private static final float[] inVec = new float[4];
/*  40 */   private static final float[] outVec = new float[4];
/*     */   
/*     */   public float x;
/*     */   public float y;
/*     */   public float z;
/*     */   
/*     */   public Vector() {}
/*     */   
/*  48 */   public Vector(float x, float y) { this(x, y, 0.0F); }
/*     */ 
/*     */ 
/*     */   
/*  52 */   public Vector(Vector v) { this(v.x, v.y, v.z); }
/*     */ 
/*     */   
/*     */   public Vector(float x, float y, float z) {
/*  56 */     this.x = x;
/*  57 */     this.y = y;
/*  58 */     this.z = z;
/*     */   }
/*     */ 
/*     */   
/*  62 */   public static Vector vector(float x, float y, float z) { return new Vector(x, y, z); }
/*     */ 
/*     */ 
/*     */   
/*  66 */   public static Vector vector(Vector v) { return new Vector(v.x, v.y, v.z); }
/*     */ 
/*     */   
/*     */   public Vector clone() throws CloneNotSupportedException {
/*  70 */     Vector result = (Vector)super.clone();
/*  71 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector clear(float scalar) {
/*  80 */     this.x = this.y = this.z = scalar;
/*  81 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public Vector set(float x, float y) { return set(x, y, 0.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public Vector set(Vector v) { return set(v.x, v.y, v.z); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   public Vector set(Vector v, float a) { return set(v.x, v.y, v.z, a); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector set(float x, float y, float z) {
/* 121 */     this.x = x;
/* 122 */     this.y = y;
/* 123 */     this.z = z;
/* 124 */     return this;
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
/*     */   public Vector set(float x, float y, float z, float a) {
/* 136 */     this.x = x * a;
/* 137 */     this.y = y * a;
/* 138 */     this.z = z * a;
/* 139 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   public float x() { return this.x; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 155 */   public void x(float x) { this.x = x; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 163 */   public float y() { return this.y; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 171 */   public void y(float y) { this.y = y; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 179 */   public float z() { return this.z; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 187 */   public void z(float z) { this.z = z; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 197 */   public Vector add(float x, float y) { return add(x, y, 0.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector add(float x, float y, float z) {
/* 208 */     this.x += x;
/* 209 */     this.y += y;
/* 210 */     this.z += z;
/* 211 */     return this;
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
/*     */   public Vector add(float x, float y, float z, float a) {
/* 223 */     this.x += x * a;
/* 224 */     this.y += y * a;
/* 225 */     this.z += z * a;
/* 226 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 235 */   public Vector add(Vector v) { return add(v.x, v.y, v.z); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 245 */   public Vector add(Vector v, float a) { return add(v.x, v.y, v.z, a); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 255 */   public Vector sub(float x, float y) { return add(-x, -y, 0.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 264 */   public Vector sub(Vector v) { return add(-v.x, -v.y, -v.z); }
/*     */ 
/*     */ 
/*     */   
/* 268 */   public Vector sub(Vector v, float a) { return add(-v.x, -v.y, -v.z, a); }
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
/* 279 */   public Vector sub(float x, float y, float z) { return add(-x, -y, -z); }
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
/*     */   
/* 291 */   public Vector sub(float x, float y, float z, float a) { return add(-x, -y, -z, a); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector mult(Vector other) {
/* 300 */     this.x *= other.x;
/* 301 */     this.y *= other.y;
/* 302 */     this.z *= other.z;
/* 303 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector mult(float scale) {
/* 310 */     this.x *= scale;
/* 311 */     this.y *= scale;
/* 312 */     this.z *= scale;
/* 313 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector mult(float x_scale, float y_scale) {
/* 320 */     this.x *= x_scale;
/* 321 */     this.y *= y_scale;
/* 322 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector mult(float x_scale, float y_scale, float z_scale) {
/* 329 */     this.x *= x_scale;
/* 330 */     this.y *= y_scale;
/* 331 */     this.z *= z_scale;
/* 332 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector div(Vector other) {
/* 341 */     this.x /= other.x;
/* 342 */     this.y /= other.y;
/* 343 */     this.z /= other.z;
/* 344 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector div(float scale) {
/* 350 */     this.x /= scale;
/* 351 */     this.y /= scale;
/* 352 */     this.z /= scale;
/* 353 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector div(float x_scale, float y_scale) {
/* 360 */     this.x /= x_scale;
/* 361 */     this.y /= y_scale;
/* 362 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector div(float x_scale, float y_scale, float z_scale) {
/* 369 */     this.x /= x_scale;
/* 370 */     this.y /= y_scale;
/* 371 */     this.z /= z_scale;
/* 372 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector mod(float scalar) {
/* 379 */     this.x %= scalar;
/* 380 */     this.y %= scalar;
/* 381 */     this.z %= scalar;
/* 382 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 390 */   public Vector toRadian() { return mult(0.017453292F); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 398 */   public Vector toDegree() { return mult(57.29578F); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector limit(float scalar) {
/* 407 */     this.x = (this.x >= scalar) ? scalar : ((this.x < -scalar) ? -scalar : this.x);
/* 408 */     this.y = (this.y >= scalar) ? scalar : ((this.y < -scalar) ? -scalar : this.y);
/* 409 */     this.z = (this.z >= scalar) ? scalar : ((this.z < -scalar) ? -scalar : this.z);
/* 410 */     for (; this.x >= scalar; this.x -= scalar);
/* 411 */     for (; this.x < -scalar; this.x += scalar);
/* 412 */     for (; this.y >= scalar; this.y -= scalar);
/* 413 */     for (; this.y < -scalar; this.y += scalar);
/* 414 */     for (; this.z >= scalar; this.z -= scalar);
/* 415 */     for (; this.z < -scalar; this.z += scalar);
/* 416 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector limit(float lower, float upper) {
/* 426 */     this.x = (this.x >= upper) ? upper : ((this.x < lower) ? lower : this.x);
/* 427 */     this.y = (this.y >= upper) ? upper : ((this.y < lower) ? lower : this.y);
/* 428 */     this.z = (this.z >= upper) ? upper : ((this.z < lower) ? lower : this.z);
/* 429 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 436 */   public float len() { return (float)Math.sqrt((this.x * this.x + this.y * this.y + this.z * this.z)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 443 */   public float lenSquared() { return this.x * this.x + this.y * this.y + this.z * this.z; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector normalize() {
/* 450 */     float len = len();
/* 451 */     if (len != 0.0F) {
/* 452 */       this.x /= len;
/* 453 */       this.y /= len;
/* 454 */       this.z /= len;
/*     */     } 
/* 456 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 464 */   public float dot(Vector v) { return this.x * v.x + this.y * v.y + this.z * v.z; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 472 */   public float dotProduct(Vector v) { return this.x * v.x + this.y * v.y + this.z * v.z; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 479 */   public float dot(float x, float y, float z) { return this.x * x + this.y * y + this.z * z; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 486 */   public float dotProduct(float x, float y, float z) { return this.x * x + this.y * y + this.z * z; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 494 */   public float cross2(Vector v) { return this.x * v.y - v.x * this.y; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 502 */   public float crossProduct2(Vector v) { return this.x * v.y - v.x * this.y; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 511 */   public Vector cross(Vector v) { return crossProduct(this, this, v); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 520 */   public Vector crossProduct(Vector v) { return crossProduct(this, this, v); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vector cross(Vector v3, Vector v1, Vector v2) {
/* 527 */     float x3 = v1.y * v2.z - v1.z * v2.y;
/* 528 */     float y3 = v1.z * v2.x - v1.x * v2.z;
/* 529 */     float z3 = v1.x * v2.y - v1.y * v2.x;
/* 530 */     v3.x = x3; v3.y = y3; v3.z = z3;
/* 531 */     return v3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vector crossProduct(Vector v3, Vector v1, Vector v2) {
/* 538 */     float x3 = v1.y * v2.z - v1.z * v2.y;
/* 539 */     float y3 = v1.z * v2.x - v1.x * v2.z;
/* 540 */     float z3 = v1.x * v2.y - v1.y * v2.x;
/* 541 */     v3.x = x3; v3.y = y3; v3.z = z3;
/* 542 */     return v3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float angleXY() {
/* 549 */     float angle = (float)Math.atan2(this.y, this.x) * 57.29578F;
/* 550 */     if (angle < 0.0F)
/* 551 */       angle += 360.0F; 
/* 552 */     return angle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float angleXZ() {
/* 559 */     float angle = (float)Math.atan2(this.z, this.x) * 57.29578F;
/* 560 */     if (angle < 0.0F)
/* 561 */       angle += 360.0F; 
/* 562 */     return angle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float angleYZ() {
/* 569 */     float angle = (float)Math.atan2(this.z, this.y) * 57.29578F;
/* 570 */     if (angle < 0.0F)
/* 571 */       angle += 360.0F; 
/* 572 */     return angle;
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
/*     */   
/*     */   public float getAngle(Vector v) {
/* 585 */     double cos = (dotProduct(v) / (float)Math.sqrt((lenSquared() * v.lenSquared())));
/* 586 */     return (float)Math.acos(cos) * 57.29578F;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector rotateXY(float angle) {
/* 591 */     float rad = angle * 0.017453292F;
/* 592 */     float cos = (float)Math.cos(rad);
/* 593 */     float sin = (float)Math.sin(rad);
/*     */     
/* 595 */     float newX = this.x * cos - this.y * sin;
/* 596 */     float newY = this.x * sin + this.y * cos;
/*     */     
/* 598 */     this.x = newX;
/* 599 */     this.y = newY;
/*     */     
/* 601 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector rotateXZ(float angle) {
/* 608 */     float rad = angle * 0.017453292F;
/* 609 */     float cos = (float)Math.cos(rad);
/* 610 */     float sin = (float)Math.sin(rad);
/*     */     
/* 612 */     float newX = this.x * cos - this.z * sin;
/* 613 */     float newZ = this.x * sin + this.z * cos;
/*     */     
/* 615 */     this.x = newX;
/* 616 */     this.z = newZ;
/*     */     
/* 618 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector rotateYZ(float angle) {
/* 625 */     float rad = angle * 0.017453292F;
/* 626 */     float cos = (float)Math.cos(rad);
/* 627 */     float sin = (float)Math.sin(rad);
/*     */     
/* 629 */     float newY = this.y * cos - this.z * sin;
/* 630 */     float newZ = this.y * sin + this.z * cos;
/*     */     
/* 632 */     this.y = newY;
/* 633 */     this.z = newZ;
/*     */     
/* 635 */     return this;
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
/*     */   
/*     */   public Vector rotate(float angle, float axisX, float axisY, float axisZ) {
/* 648 */     inVec[0] = this.x;
/* 649 */     inVec[1] = this.y;
/* 650 */     inVec[2] = this.z;
/* 651 */     inVec[3] = 1.0F;
/* 652 */     Matrix.setIdentityM(matrix, 0);
/* 653 */     Matrix.rotateM(matrix, 0, angle, axisX, axisY, axisZ);
/* 654 */     Matrix.multiplyMV(outVec, 0, matrix, 0, inVec, 0);
/* 655 */     this.x = outVec[0];
/* 656 */     this.y = outVec[1];
/* 657 */     this.z = outVec[2];
/* 658 */     return this;
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
/* 669 */   public Vector rotate(float angleX, float angleY, float angleZ) { return rotate(this, angleX, angleY, angleZ); }
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
/*     */   public static Vector rotate(Vector v, float angleX, float angleY, float angleZ) {
/* 681 */     inVec[0] = v.x;
/* 682 */     inVec[1] = v.y;
/* 683 */     inVec[2] = v.z;
/* 684 */     inVec[3] = 1.0F;
/* 685 */     Matrix.setIdentityM(matrix, 0);
/* 686 */     if (angleX != 0.0F)
/* 687 */       Matrix.rotateM(matrix, 0, angleX, 1.0F, 0.0F, 0.0F); 
/* 688 */     if (angleY != 0.0F)
/* 689 */       Matrix.rotateM(matrix, 0, angleY, 0.0F, 1.0F, 0.0F); 
/* 690 */     if (angleZ != 0.0F)
/* 691 */       Matrix.rotateM(matrix, 0, angleZ, 0.0F, 0.0F, 1.0F); 
/* 692 */     Matrix.multiplyMV(outVec, 0, matrix, 0, inVec, 0);
/* 693 */     v.x = outVec[0];
/* 694 */     v.y = outVec[1];
/* 695 */     v.z = outVec[2];
/* 696 */     return v;
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
/*     */   public static Vector[] rotate(Vector[] v, float angleX, float angleY, float angleZ) {
/* 708 */     Matrix.setIdentityM(matrix, 0);
/* 709 */     if (angleX != 0.0F)
/* 710 */       Matrix.rotateM(matrix, 0, angleX, 1.0F, 0.0F, 0.0F); 
/* 711 */     if (angleY != 0.0F)
/* 712 */       Matrix.rotateM(matrix, 0, angleY, 0.0F, 1.0F, 0.0F); 
/* 713 */     if (angleZ != 0.0F)
/* 714 */       Matrix.rotateM(matrix, 0, angleZ, 0.0F, 0.0F, 1.0F); 
/* 715 */     int n = (v != null) ? v.length : 0;
/* 716 */     for (int i = 0; i < n; i++) {
/* 717 */       if (v[i] != null) {
/* 718 */         inVec[0] = (v[i]).x;
/* 719 */         inVec[1] = (v[i]).y;
/* 720 */         inVec[2] = (v[i]).z;
/* 721 */         inVec[3] = 1.0F;
/* 722 */         Matrix.multiplyMV(outVec, 0, matrix, 0, inVec, 0);
/* 723 */         (v[i]).x = outVec[0];
/* 724 */         (v[i]).y = outVec[1];
/* 725 */         (v[i]).z = outVec[2];
/*     */       } 
/* 727 */     }  return v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector rotate(Vector angle, float a) {
/* 737 */     rotate(angle.x * a, angle.y * a, angle.z * a);
/* 738 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 742 */   public Vector rotate(Vector angle) { return rotate(angle.x, angle.y, angle.z); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector rotate_inv(float angleX, float angleY, float angleZ) {
/* 753 */     inVec[0] = this.x;
/* 754 */     inVec[1] = this.y;
/* 755 */     inVec[2] = this.z;
/* 756 */     inVec[3] = 1.0F;
/* 757 */     Matrix.setIdentityM(matrix, 0);
/* 758 */     if (angleZ != 0.0F)
/* 759 */       Matrix.rotateM(matrix, 0, angleZ, 0.0F, 0.0F, 1.0F); 
/* 760 */     if (angleY != 0.0F)
/* 761 */       Matrix.rotateM(matrix, 0, angleY, 0.0F, 1.0F, 0.0F); 
/* 762 */     if (angleX != 0.0F)
/* 763 */       Matrix.rotateM(matrix, 0, angleX, 1.0F, 0.0F, 0.0F); 
/* 764 */     Matrix.multiplyMV(outVec, 0, matrix, 0, inVec, 0);
/* 765 */     this.x = outVec[0];
/* 766 */     this.y = outVec[1];
/* 767 */     this.z = outVec[2];
/* 768 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector rotate_inv(Vector angle, float a) {
/* 778 */     rotate_inv(angle.x * a, angle.y * a, angle.z * a);
/* 779 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector rotate_inv(Vector angle) {
/* 788 */     rotate_inv(angle, -1.0F);
/* 789 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float[] getQuat() {
/* 797 */     float[] q = new float[4];
/* 798 */     q[0] = this.x;
/* 799 */     q[1] = this.y;
/* 800 */     q[2] = this.z;
/* 801 */     q[3] = 1.0F;
/* 802 */     return q;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector setQuat(float[] q) {
/* 811 */     this.x = q[0];
/* 812 */     this.y = q[1];
/* 813 */     this.z = q[2];
/* 814 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 821 */   public float distance(Vector v) { return distance(v.x, v.y, v.z); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 831 */   public float distance(float x, float y) { return distance(x, y, this.z); }
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
/* 842 */   public float distance(float x, float y, float z) { return (float)Math.sqrt(distSquared(x, y, z)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 851 */   public float distSquared(Vector v) { return distSquared(v.x, v.y, v.z); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 861 */   public float distSquared(float x, float y) { return distSquared(x, y, this.z); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float distSquared(float x, float y, float z) {
/* 872 */     float dx = this.x - x;
/* 873 */     float dy = this.y - y;
/* 874 */     float dz = this.z - z;
/* 875 */     return dx * dx + dy * dy + dz * dz;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector swap(Vector v) {
/* 882 */     float w = this.x; this.x = v.x; v.x = w;
/* 883 */     w = this.y; this.y = v.y; v.y = w;
/* 884 */     w = this.z; this.z = v.z; v.z = w;
/* 885 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector swapXY() {
/* 892 */     float w = this.x; this.x = this.y; this.y = w;
/* 893 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float slope(Vector v) {
/* 900 */     if (v.x != this.x) {
/* 901 */       return (v.y - this.y) / (v.x - this.x);
/*     */     }
/* 903 */     return (v.y - this.y >= 0.0F) ? Float.MAX_VALUE : Float.MIN_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float slope() {
/* 911 */     if (this.x != 0.0F) {
/* 912 */       return this.y / this.x;
/*     */     }
/* 914 */     return (this.y >= 0.0F) ? Float.MAX_VALUE : Float.MIN_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector sign() {
/* 922 */     this.x = Math.signum(this.x);
/* 923 */     this.y = Math.signum(this.y);
/* 924 */     this.z = Math.signum(this.z);
/* 925 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 930 */   public String toString() { return String.format(Locale.US, "(%f,%f,%f)", new Object[] { Float.valueOf(this.x), Float.valueOf(this.y), Float.valueOf(this.z) }); }
/*     */ 
/*     */ 
/*     */   
/* 934 */   public String toString(String fmt) { return String.format(Locale.US, fmt, new Object[] { Float.valueOf(this.x), Float.valueOf(this.y), Float.valueOf(this.z) }); }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\math\Vector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */