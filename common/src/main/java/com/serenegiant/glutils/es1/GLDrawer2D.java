/*     */ package com.serenegiant.glutils.es1;
/*     */ 
/*     */ import android.opengl.GLES10;
/*     */ import android.opengl.Matrix;
/*     */ import com.serenegiant.glutils.IDrawer2D;
/*     */ import com.serenegiant.glutils.ITexture;
/*     */ import com.serenegiant.glutils.TextureOffscreen;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.FloatBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GLDrawer2D
/*     */   implements IDrawer2D
/*     */ {
/*  21 */   private static final float[] VERTICES = new float[] { 1.0F, 1.0F, -1.0F, 1.0F, 1.0F, -1.0F, -1.0F, -1.0F };
/*  22 */   private static final float[] TEXCOORD = new float[] { 1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F };
/*     */   
/*     */   private static final int FLOAT_SZ = 4;
/*     */   private static final int VERTEX_NUM = 4;
/*     */   private static final int VERTEX_SZ = 8;
/*  27 */   private final float[] mMvpMatrix = new float[16];
/*     */ 
/*     */   
/*     */   private final FloatBuffer pVertex;
/*     */   
/*     */   private final FloatBuffer pTexCoord;
/*     */   
/*     */   private final int mTexTarget;
/*     */ 
/*     */   
/*     */   public GLDrawer2D(boolean isOES) {
/*  38 */     this.mTexTarget = isOES ? 36197 : 3553;
/*  39 */     this
/*  40 */       .pVertex = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
/*  41 */     this.pVertex.put(VERTICES);
/*  42 */     this.pVertex.flip();
/*  43 */     this
/*  44 */       .pTexCoord = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
/*  45 */     this.pTexCoord.put(TEXCOORD);
/*  46 */     this.pTexCoord.flip();
/*     */     
/*  48 */     Matrix.setIdentityM(this.mMvpMatrix, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void release() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public float[] getMvpMatrix() { return this.mMvpMatrix; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IDrawer2D setMvpMatrix(float[] matrix, int offset) {
/*  73 */     System.arraycopy(matrix, offset, this.mMvpMatrix, 0, 16);
/*  74 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   public void getMvpMatrix(float[] matrix, int offset) { System.arraycopy(this.mMvpMatrix, 0, matrix, offset, 16); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void draw(int texId, float[] tex_matrix, int offset) {
/*  90 */     GLES10.glEnableClientState(32884);
/*  91 */     this.pVertex.position(0);
/*  92 */     GLES10.glVertexPointer(2, 5126, 8, this.pVertex);
/*     */     
/*  94 */     GLES10.glEnableClientState(32888);
/*  95 */     this.pTexCoord.position(0);
/*  96 */     GLES10.glTexCoordPointer(4, 5126, 8, this.pTexCoord);
/*  97 */     GLES10.glActiveTexture(33984);
/*  98 */     GLES10.glBindTexture(this.mTexTarget, texId);
/*     */     
/* 100 */     GLES10.glDrawArrays(5, 0, 4);
/*     */     
/* 102 */     GLES10.glBindTexture(this.mTexTarget, 0);
/* 103 */     GLES10.glDisableClientState(32888);
/*     */     
/* 105 */     GLES10.glDisableClientState(32884);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 110 */   public void draw(ITexture texture) { draw(texture.getTexture(), texture.getTexMatrix(), 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   public void draw(TextureOffscreen offscreen) { draw(offscreen.getTexture(), offscreen.getTexMatrix(), 0); }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\es1\GLDrawer2D.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */