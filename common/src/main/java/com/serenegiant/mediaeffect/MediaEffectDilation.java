/*     */ package com.serenegiant.mediaeffect;
/*     */ 
/*     */ import android.opengl.GLES20;
/*     */ import com.serenegiant.glutils.GLHelper;
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
/*     */ public class MediaEffectDilation
/*     */   extends MediaEffectGLESBase
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private static final String TAG = "MediaEffectDilation";
/*     */   public static final String FRAGMENT_SHADER_1 = "precision lowp float;\nvarying       vec2 vTextureCoord;\nuniform vec2  uTexOffset[41];\nuniform sampler2D sTexture;\n\nvoid main()\n{\nvec4 maxValue = texture2D(sTexture, vTextureCoord + uTexOffset[0] );\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[1] ));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[2] ));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[3] ));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[4] ));\n\ngl_FragColor = vec4(maxValue.rgb, 1.0);\n}\n";
/*     */   public static final String FRAGMENT_SHADER_2 = "precision lowp float;\n\nvarying       vec2 vTextureCoord;\nuniform vec2  uTexOffset[41];\nuniform sampler2D sTexture;\n\nvoid main()\n{\nvec4 maxValue = texture2D(sTexture, vTextureCoord + uTexOffset[0]);\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[1]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[2]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[3]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[4]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[5]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[6]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[7]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[8]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[9]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[10]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[11]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[12]));\n\ngl_FragColor = vec4(maxValue.rgb, 1.0);\n}\n";
/*     */   public static final String FRAGMENT_SHADER_3 = "precision lowp float;\nvarying       vec2 vTextureCoord;\nuniform vec2  uTexOffset[41];\nuniform sampler2D sTexture;\n\nvoid main()\n{\nvec4 maxValue = texture2D(sTexture, vTextureCoord + uTexOffset[0]);\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[1]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[2]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[3]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[4]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[5]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[6]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[7]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[8]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[9]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[10]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[11]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[12]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[13]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[14]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[15]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[16]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[17]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[18]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[19]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[20]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[21]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[22]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[23]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[24]));\n\ngl_FragColor = vec4(maxValue.rgb, 1.0);\n}\n";
/*     */   public static final String FRAGMENT_SHADER_4 = "precision lowp float;\nvarying       vec2 vTextureCoord;\nuniform vec2  uTexOffset[41];\nuniform sampler2D sTexture;\n\nvoid main()\n{\nvec4 maxValue = texture2D(sTexture, vTextureCoord + uTexOffset[0]);\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[1]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[2]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[3]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[4]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[5]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[6]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[7]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[8]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[9]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[10]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[11]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[12]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[13]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[14]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[15]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[16]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[17]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[18]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[19]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[20]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[21]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[22]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[23]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[24]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[25]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[26]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[27]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[28]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[29]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[30]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[31]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[32]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[33]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[34]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[35]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[36]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[37]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[38]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[39]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[40]));\n\ngl_FragColor = vec4(maxValue.rgb, 1.0);\n}\n";
/*     */   
/*     */   private static class MediaEffectDilationDrawer
/*     */     extends MediaEffectDrawer
/*     */   {
/*     */     private final int muTexOffsetLoc;
/*  34 */     private final float[] mTexOffset = new float[82];
/*     */     private float mTexWidth;
/*     */     
/*     */     public MediaEffectDilationDrawer(String fss) {
/*  38 */       super(false, "#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", fss);
/*  39 */       this.muTexOffsetLoc = GLES20.glGetUniformLocation(getProgram(), "uTexOffset");
/*  40 */       GLHelper.checkLocation(this.muTexOffsetLoc, "uTexOffset");
/*     */     }
/*     */     private float mTexHeight;
/*     */     
/*     */     protected void preDraw(int tex_id, float[] tex_matrix, int offset) {
/*  45 */       super.preDraw(tex_id, tex_matrix, offset);
/*     */       
/*  47 */       if (this.muTexOffsetLoc >= 0) {
/*  48 */         GLES20.glUniform2fv(this.muTexOffsetLoc, 41, this.mTexOffset, 0);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setTexSize(int width, int height) {
/*  56 */       synchronized (this.mSync) {
/*  57 */         this.mTexHeight = height;
/*  58 */         this.mTexWidth = width;
/*  59 */         float rw = 1.0F / width;
/*  60 */         float rh = 1.0F / height;
/*     */ 
/*     */         
/*  63 */         this.mTexOffset[0] = 0.0F; this.mTexOffset[1] = 0.0F;
/*     */         
/*  65 */         this.mTexOffset[2] = 0.0F; this.mTexOffset[3] = -rh;
/*  66 */         this.mTexOffset[4] = 0.0F; this.mTexOffset[5] = rh;
/*  67 */         this.mTexOffset[6] = -rw; this.mTexOffset[7] = 0.0F;
/*  68 */         this.mTexOffset[8] = rw; this.mTexOffset[9] = 0.0F;
/*     */         
/*  70 */         this.mTexOffset[10] = 0.0F; this.mTexOffset[11] = -rh * 2.0F;
/*  71 */         this.mTexOffset[12] = 0.0F; this.mTexOffset[13] = rh * 2.0F;
/*  72 */         this.mTexOffset[14] = -rw * 2.0F; this.mTexOffset[15] = 0.0F;
/*  73 */         this.mTexOffset[16] = rw * 2.0F; this.mTexOffset[17] = 0.0F;
/*  74 */         this.mTexOffset[18] = -rw; this.mTexOffset[19] = -rh;
/*  75 */         this.mTexOffset[20] = -rw; this.mTexOffset[21] = rh;
/*  76 */         this.mTexOffset[22] = rw; this.mTexOffset[23] = -rh;
/*  77 */         this.mTexOffset[24] = rw; this.mTexOffset[25] = rh;
/*     */         
/*  79 */         this.mTexOffset[26] = 0.0F; this.mTexOffset[27] = -rh * 3.0F;
/*  80 */         this.mTexOffset[28] = 0.0F; this.mTexOffset[29] = rh * 3.0F;
/*  81 */         this.mTexOffset[30] = -rw * 3.0F; this.mTexOffset[31] = 0.0F;
/*  82 */         this.mTexOffset[32] = rw * 3.0F; this.mTexOffset[33] = 0.0F;
/*  83 */         this.mTexOffset[34] = -rw * 2.0F; this.mTexOffset[35] = -rh;
/*  84 */         this.mTexOffset[36] = -rw * 2.0F; this.mTexOffset[37] = rh;
/*  85 */         this.mTexOffset[38] = rw * 2.0F; this.mTexOffset[39] = -rh;
/*  86 */         this.mTexOffset[40] = rw * 2.0F; this.mTexOffset[41] = rh;
/*  87 */         this.mTexOffset[42] = -rw; this.mTexOffset[43] = -rh * 2.0F;
/*  88 */         this.mTexOffset[44] = -rw; this.mTexOffset[45] = rh * 2.0F;
/*  89 */         this.mTexOffset[46] = rw; this.mTexOffset[47] = -rh * 2.0F;
/*  90 */         this.mTexOffset[48] = rw; this.mTexOffset[49] = rh * 2.0F;
/*     */         
/*  92 */         this.mTexOffset[50] = 0.0F; this.mTexOffset[51] = -rh * 4.0F;
/*  93 */         this.mTexOffset[52] = 0.0F; this.mTexOffset[53] = rh * 4.0F;
/*  94 */         this.mTexOffset[54] = -rw * 4.0F; this.mTexOffset[55] = 0.0F;
/*  95 */         this.mTexOffset[56] = rw * 4.0F; this.mTexOffset[57] = 0.0F;
/*  96 */         this.mTexOffset[58] = -rw * 3.0F; this.mTexOffset[59] = -rh;
/*  97 */         this.mTexOffset[60] = -rw * 3.0F; this.mTexOffset[61] = rh;
/*  98 */         this.mTexOffset[62] = rw * 3.0F; this.mTexOffset[63] = -rh;
/*  99 */         this.mTexOffset[64] = rw * 3.0F; this.mTexOffset[65] = rh;
/* 100 */         this.mTexOffset[66] = -rw * 2.0F; this.mTexOffset[67] = -rh * 2.0F;
/* 101 */         this.mTexOffset[68] = -rw * 2.0F; this.mTexOffset[69] = rh * 2.0F;
/* 102 */         this.mTexOffset[70] = rw * 2.0F; this.mTexOffset[71] = -rh * 2.0F;
/* 103 */         this.mTexOffset[72] = rw * 2.0F; this.mTexOffset[73] = rh * 2.0F;
/* 104 */         this.mTexOffset[74] = -rw; this.mTexOffset[75] = -rh * 3.0F;
/* 105 */         this.mTexOffset[76] = -rw; this.mTexOffset[77] = rh * 3.0F;
/* 106 */         this.mTexOffset[78] = rw; this.mTexOffset[79] = -rh * 3.0F;
/* 107 */         this.mTexOffset[80] = rw; this.mTexOffset[81] = rh * 3.0F;
/*     */       } 
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getFragmentShader(int radius) {
/* 246 */     switch (radius) {
/*     */       case 0:
/*     */       case 1:
/* 249 */         return "precision lowp float;\nvarying       vec2 vTextureCoord;\nuniform vec2  uTexOffset[41];\nuniform sampler2D sTexture;\n\nvoid main()\n{\nvec4 maxValue = texture2D(sTexture, vTextureCoord + uTexOffset[0] );\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[1] ));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[2] ));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[3] ));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[4] ));\n\ngl_FragColor = vec4(maxValue.rgb, 1.0);\n}\n";
/*     */       case 2:
/* 251 */         return "precision lowp float;\n\nvarying       vec2 vTextureCoord;\nuniform vec2  uTexOffset[41];\nuniform sampler2D sTexture;\n\nvoid main()\n{\nvec4 maxValue = texture2D(sTexture, vTextureCoord + uTexOffset[0]);\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[1]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[2]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[3]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[4]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[5]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[6]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[7]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[8]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[9]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[10]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[11]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[12]));\n\ngl_FragColor = vec4(maxValue.rgb, 1.0);\n}\n";
/*     */       case 3:
/* 253 */         return "precision lowp float;\nvarying       vec2 vTextureCoord;\nuniform vec2  uTexOffset[41];\nuniform sampler2D sTexture;\n\nvoid main()\n{\nvec4 maxValue = texture2D(sTexture, vTextureCoord + uTexOffset[0]);\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[1]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[2]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[3]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[4]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[5]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[6]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[7]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[8]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[9]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[10]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[11]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[12]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[13]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[14]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[15]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[16]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[17]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[18]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[19]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[20]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[21]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[22]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[23]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[24]));\n\ngl_FragColor = vec4(maxValue.rgb, 1.0);\n}\n";
/*     */     } 
/* 255 */     return "precision lowp float;\nvarying       vec2 vTextureCoord;\nuniform vec2  uTexOffset[41];\nuniform sampler2D sTexture;\n\nvoid main()\n{\nvec4 maxValue = texture2D(sTexture, vTextureCoord + uTexOffset[0]);\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[1]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[2]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[3]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[4]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[5]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[6]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[7]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[8]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[9]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[10]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[11]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[12]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[13]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[14]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[15]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[16]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[17]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[18]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[19]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[20]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[21]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[22]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[23]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[24]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[25]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[26]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[27]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[28]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[29]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[30]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[31]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[32]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[33]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[34]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[35]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[36]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[37]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[38]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[39]));\nmaxValue = max(maxValue, texture2D(sTexture, vTextureCoord + uTexOffset[40]));\n\ngl_FragColor = vec4(maxValue.rgb, 1.0);\n}\n";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 260 */   public MediaEffectDilation() { this(1); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaEffectDilation(int radius) {
/* 268 */     super(new MediaEffectDilationDrawer(getFragmentShader(radius)));
/* 269 */     setTexSize(256, 256);
/*     */   }
/*     */ 
/*     */   
/*     */   public MediaEffectDilation resize(int width, int height) {
/* 274 */     super.resize(width, height);
/* 275 */     setTexSize(width, height);
/* 276 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 283 */   public void setTexSize(int width, int height) { this.mDrawer.setTexSize(width, height); }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectDilation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */