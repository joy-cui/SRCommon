/*     */ package com.serenegiant.glutils;
/*     */ 
/*     */ import android.opengl.GLES20;
/*     */ import android.view.MotionEvent;
/*     */ import java.nio.FloatBuffer;
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
/*     */ public class Texture2dProgram
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private static final String TAG = "Texture2dProgram";
/*     */   
/*     */   public enum ProgramType
/*     */   {
/*  37 */     TEXTURE_2D,
/*     */ 
/*     */     
/*  40 */     TEXTURE_FILT3x3,
/*  41 */     TEXTURE_CUSTOM,
/*     */     
/*  43 */     TEXTURE_EXT,
/*  44 */     TEXTURE_EXT_BW,
/*  45 */     TEXTURE_EXT_NIGHT,
/*  46 */     TEXTURE_EXT_CHROMA_KEY,
/*  47 */     TEXTURE_EXT_SQUEEZE,
/*  48 */     TEXTURE_EXT_TWIRL,
/*  49 */     TEXTURE_EXT_TUNNEL,
/*  50 */     TEXTURE_EXT_BULGE,
/*  51 */     TEXTURE_EXT_DENT,
/*  52 */     TEXTURE_EXT_FISHEYE,
/*  53 */     TEXTURE_EXT_STRETCH,
/*  54 */     TEXTURE_EXT_MIRROR,
/*     */ 
/*     */     
/*  57 */     TEXTURE_EXT_FILT3x3;
/*     */   }
/*     */   
/*  60 */   private final Object mSync = new Object();
/*     */   
/*     */   private final ProgramType mProgramType;
/*     */   
/*     */   private float mTexWidth;
/*     */   
/*     */   private float mTexHeight;
/*     */   
/*     */   private int mProgramHandle;
/*     */   
/*     */   private final int muMVPMatrixLoc;
/*     */   private final int muTexMatrixLoc;
/*     */   private final int maPositionLoc;
/*     */   private final int maTextureCoordLoc;
/*     */   private int muKernelLoc;
/*     */   private int muTexOffsetLoc;
/*     */   private int muColorAdjustLoc;
/*     */   private int muTouchPositionLoc;
/*     */   private int muFlagsLoc;
/*     */   private int mTextureTarget;
/*     */   protected boolean mHasKernel2;
/*  81 */   private final float[] mKernel = new float[18];
/*  82 */   private final float[] mSummedTouchPosition = new float[2];
/*  83 */   private final float[] mLastTouchPosition = new float[2];
/*     */   private float[] mTexOffset;
/*     */   private float mColorAdjust;
/*  86 */   private final int[] mFlags = new int[4];
/*     */ 
/*     */   
/*  89 */   public Texture2dProgram(int target, String fss) { this(ProgramType.TEXTURE_CUSTOM, target, "#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", fss); }
/*     */ 
/*     */ 
/*     */   
/*  93 */   public Texture2dProgram(int target, String vss, String fss) { this(ProgramType.TEXTURE_CUSTOM, target, vss, fss); }
/*     */ 
/*     */ 
/*     */   
/*  97 */   public Texture2dProgram(ProgramType programType) { this(programType, 0, null, null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Texture2dProgram(ProgramType programType, int target, String vss, String fss) {
/* 104 */     this.mProgramType = programType;
/*     */     
/* 106 */     float[] kernel = null, kernel2 = null;
/* 107 */     switch (programType) {
/*     */       case TEXTURE_2D:
/* 109 */         this.mTextureTarget = 3553;
/* 110 */         this.mProgramHandle = GLHelper.loadShader("#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", ShaderConst.FRAGMENT_SHADER_2D);
/*     */         break;
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
/*     */       case TEXTURE_FILT3x3:
/* 125 */         this.mTextureTarget = 3553;
/* 126 */         this.mProgramHandle = GLHelper.loadShader("#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", ShaderConst.FRAGMENT_SHADER_FILT3x3);
/*     */         break;
/*     */       case TEXTURE_EXT:
/* 129 */         this.mTextureTarget = 36197;
/* 130 */         this.mProgramHandle = GLHelper.loadShader("#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", ShaderConst.FRAGMENT_SHADER_EXT);
/*     */         break;
/*     */       case TEXTURE_EXT_BW:
/* 133 */         this.mTextureTarget = 36197;
/* 134 */         this.mProgramHandle = GLHelper.loadShader("#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", ShaderConst.FRAGMENT_SHADER_EXT_BW);
/*     */         break;
/*     */       case TEXTURE_EXT_NIGHT:
/* 137 */         this.mTextureTarget = 36197;
/* 138 */         this.mProgramHandle = GLHelper.loadShader("#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", ShaderConst.FRAGMENT_SHADER_EXT_NIGHT);
/*     */         break;
/*     */       case TEXTURE_EXT_CHROMA_KEY:
/* 141 */         this.mTextureTarget = 36197;
/* 142 */         this.mProgramHandle = GLHelper.loadShader("#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", ShaderConst.FRAGMENT_SHADER_EXT_CHROMA_KEY);
/*     */         break;
/*     */       case TEXTURE_EXT_SQUEEZE:
/* 145 */         this.mTextureTarget = 36197;
/* 146 */         this.mProgramHandle = GLHelper.loadShader("#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", ShaderConst.FRAGMENT_SHADER_EXT_SQUEEZE);
/*     */         break;
/*     */       case TEXTURE_EXT_TWIRL:
/* 149 */         this.mTextureTarget = 36197;
/* 150 */         this.mProgramHandle = GLHelper.loadShader("#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", "#version 100\n#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nuniform vec2 uPosition;\nvoid main() {\n    vec2 texCoord = vTextureCoord.xy;\n    vec2 normCoord = 2.0 * texCoord - 1.0;\n    float r = length(normCoord); // to polar coords \n    float phi = atan(normCoord.y + uPosition.y, normCoord.x + uPosition.x); // to polar coords \n    phi = phi + (1.0 - smoothstep(-0.5, 0.5, r)) * 4.0;\n    normCoord.x = r * cos(phi); \n    normCoord.y = r * sin(phi); \n    texCoord = normCoord / 2.0 + 0.5;\n    gl_FragColor = texture2D(sTexture, texCoord);\n}\n");
/*     */         break;
/*     */       case TEXTURE_EXT_TUNNEL:
/* 153 */         this.mTextureTarget = 36197;
/* 154 */         this.mProgramHandle = GLHelper.loadShader("#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", "#version 100\n#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nuniform vec2 uPosition;\nvoid main() {\n    vec2 texCoord = vTextureCoord.xy;\n    vec2 normCoord = 2.0 * texCoord - 1.0;\n    float r = length(normCoord); // to polar coords \n    float phi = atan(normCoord.y + uPosition.y, normCoord.x + uPosition.x); // to polar coords \n    if (r > 0.5) r = 0.5;\n    normCoord.x = r * cos(phi); \n    normCoord.y = r * sin(phi); \n    texCoord = normCoord / 2.0 + 0.5;\n    gl_FragColor = texture2D(sTexture, texCoord);\n}\n");
/*     */         break;
/*     */       case TEXTURE_EXT_BULGE:
/* 157 */         this.mTextureTarget = 36197;
/* 158 */         this.mProgramHandle = GLHelper.loadShader("#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", "#version 100\n#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nuniform vec2 uPosition;\nvoid main() {\n    vec2 texCoord = vTextureCoord.xy;\n    vec2 normCoord = 2.0 * texCoord - 1.0;\n    float r = length(normCoord); // to polar coords \n    float phi = atan(normCoord.y + uPosition.y, normCoord.x + uPosition.x); // to polar coords \n    r = r * smoothstep(-0.1, 0.5, r);\n    normCoord.x = r * cos(phi); \n    normCoord.y = r * sin(phi); \n    texCoord = normCoord / 2.0 + 0.5;\n    gl_FragColor = texture2D(sTexture, texCoord);\n}\n");
/*     */         break;
/*     */       case TEXTURE_EXT_FISHEYE:
/* 161 */         this.mTextureTarget = 36197;
/* 162 */         this.mProgramHandle = GLHelper.loadShader("#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", "#version 100\n#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nuniform vec2 uPosition;\nvoid main() {\n    vec2 texCoord = vTextureCoord.xy;\n    vec2 normCoord = 2.0 * texCoord - 1.0;\n    float r = length(normCoord); // to polar coords \n    float phi = atan(normCoord.y + uPosition.y, normCoord.x + uPosition.x); // to polar coords \n    r = r * r / sqrt(2.0);\n    normCoord.x = r * cos(phi); \n    normCoord.y = r * sin(phi); \n    texCoord = normCoord / 2.0 + 0.5;\n    gl_FragColor = texture2D(sTexture, texCoord);\n}\n");
/*     */         break;
/*     */       case TEXTURE_EXT_DENT:
/* 165 */         this.mTextureTarget = 36197;
/* 166 */         this.mProgramHandle = GLHelper.loadShader("#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", "#version 100\n#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nuniform vec2 uPosition;\nvoid main() {\n    vec2 texCoord = vTextureCoord.xy;\n    vec2 normCoord = 2.0 * texCoord - 1.0;\n    float r = length(normCoord); // to polar coords \n    float phi = atan(normCoord.y + uPosition.y, normCoord.x + uPosition.x); // to polar coords \n    r = 2.0 * r - r * smoothstep(0.0, 0.7, r);\n    normCoord.x = r * cos(phi); \n    normCoord.y = r * sin(phi); \n    texCoord = normCoord / 2.0 + 0.5;\n    gl_FragColor = texture2D(sTexture, texCoord);\n}\n");
/*     */         break;
/*     */       case TEXTURE_EXT_MIRROR:
/* 169 */         this.mTextureTarget = 36197;
/* 170 */         this.mProgramHandle = GLHelper.loadShader("#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", "#version 100\n#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nuniform vec2 uPosition;\nvoid main() {\n    vec2 texCoord = vTextureCoord.xy;\n    vec2 normCoord = 2.0 * texCoord - 1.0;\n    normCoord.x = normCoord.x * sign(normCoord.x + uPosition.x);\n    texCoord = normCoord / 2.0 + 0.5;\n    gl_FragColor = texture2D(sTexture, texCoord);\n}\n");
/*     */         break;
/*     */       case TEXTURE_EXT_STRETCH:
/* 173 */         this.mTextureTarget = 36197;
/* 174 */         this.mProgramHandle = GLHelper.loadShader("#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", "#version 100\n#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nuniform vec2 uPosition;\nvoid main() {\n    vec2 texCoord = vTextureCoord.xy;\n    vec2 normCoord = 2.0 * texCoord - 1.0;\n    vec2 s = sign(normCoord + uPosition);\n    normCoord = abs(normCoord);\n    normCoord = 0.5 * normCoord + 0.5 * smoothstep(0.25, 0.5, normCoord) * normCoord;\n    normCoord = s * normCoord;\n    texCoord = normCoord / 2.0 + 0.5;\n    gl_FragColor = texture2D(sTexture, texCoord);\n}\n");
/*     */         break;
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
/*     */       case TEXTURE_EXT_FILT3x3:
/* 189 */         this.mTextureTarget = 36197;
/* 190 */         this.mProgramHandle = GLHelper.loadShader("#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", ShaderConst.FRAGMENT_SHADER_EXT_FILT3x3);
/*     */         break;
/*     */       case TEXTURE_CUSTOM:
/* 193 */         switch (target) {
/*     */           case 3553:
/*     */           case 36197:
/*     */             break;
/*     */           default:
/* 198 */             throw new IllegalArgumentException("target should be GL_TEXTURE_2D or GL_TEXTURE_EXTERNAL_OES");
/*     */         } 
/* 200 */         this.mTextureTarget = target;
/* 201 */         this.mProgramHandle = GLHelper.loadShader(vss, fss);
/*     */         break;
/*     */       default:
/* 204 */         throw new RuntimeException("Unhandled type " + programType);
/*     */     } 
/* 206 */     if (this.mProgramHandle == 0) {
/* 207 */       throw new RuntimeException("Unable to create program");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 212 */     this.maPositionLoc = GLES20.glGetAttribLocation(this.mProgramHandle, "aPosition");
/* 213 */     GLHelper.checkLocation(this.maPositionLoc, "aPosition");
/* 214 */     this.maTextureCoordLoc = GLES20.glGetAttribLocation(this.mProgramHandle, "aTextureCoord");
/* 215 */     GLHelper.checkLocation(this.maTextureCoordLoc, "aTextureCoord");
/* 216 */     this.muMVPMatrixLoc = GLES20.glGetUniformLocation(this.mProgramHandle, "uMVPMatrix");
/* 217 */     GLHelper.checkLocation(this.muMVPMatrixLoc, "uMVPMatrix");
/* 218 */     this.muTexMatrixLoc = GLES20.glGetUniformLocation(this.mProgramHandle, "uTexMatrix");
/*     */     
/* 220 */     initLocation(kernel, kernel2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void release() {
/* 229 */     GLES20.glDeleteProgram(this.mProgramHandle);
/* 230 */     this.mProgramHandle = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 237 */   public ProgramType getProgramType() { return this.mProgramType; }
/*     */ 
/*     */ 
/*     */   
/* 241 */   public int getProgramHandle() { return this.mProgramHandle; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int createTextureObject() {
/* 250 */     int[] textures = new int[1];
/* 251 */     GLES20.glGenTextures(1, textures, 0);
/* 252 */     GLHelper.checkGlError("glGenTextures");
/*     */     
/* 254 */     int texId = textures[0];
/* 255 */     GLES20.glBindTexture(this.mTextureTarget, texId);
/* 256 */     GLHelper.checkGlError("glBindTexture " + texId);
/*     */     
/* 258 */     GLES20.glTexParameterf(this.mTextureTarget, 10241, 9728.0F);
/* 259 */     GLES20.glTexParameterf(this.mTextureTarget, 10240, 9729.0F);
/* 260 */     GLES20.glTexParameteri(this.mTextureTarget, 10242, 33071);
/* 261 */     GLES20.glTexParameteri(this.mTextureTarget, 10243, 33071);
/* 262 */     GLHelper.checkGlError("glTexParameter");
/*     */     
/* 264 */     return texId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleTouchEvent(MotionEvent ev) {
/* 274 */     synchronized (this.mSync) {
/* 275 */       if (ev.getAction() == 2) {
/*     */         
/* 277 */         if (this.mTexHeight != 0.0F && this.mTexWidth != 0.0F) {
/* 278 */           this.mSummedTouchPosition[0] = this.mSummedTouchPosition[0] + 2.0F * (ev.getX() - this.mLastTouchPosition[0]) / this.mTexWidth;
/* 279 */           this.mSummedTouchPosition[1] = this.mSummedTouchPosition[1] + 2.0F * (ev.getY() - this.mLastTouchPosition[1]) / -this.mTexHeight;
/* 280 */           this.mLastTouchPosition[0] = ev.getX();
/* 281 */           this.mLastTouchPosition[1] = ev.getY();
/*     */         } 
/* 283 */       } else if (ev.getAction() == 0) {
/*     */         
/* 285 */         this.mLastTouchPosition[0] = ev.getX();
/* 286 */         this.mLastTouchPosition[1] = ev.getY();
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
/*     */   public void setKernel(float[] values, float colorAdj) {
/* 299 */     if (values.length < 9) {
/* 300 */       throw new IllegalArgumentException("Kernel size is " + values.length + " vs. " + '\t');
/*     */     }
/*     */     
/* 303 */     System.arraycopy(values, 0, this.mKernel, 0, 9);
/* 304 */     this.mColorAdjust = colorAdj;
/*     */   }
/*     */   
/*     */   public void setKernel2(float[] values) {
/* 308 */     synchronized (this.mSync) {
/* 309 */       this.mHasKernel2 = (values != null && values.length == 9);
/* 310 */       if (this.mHasKernel2) {
/* 311 */         System.arraycopy(values, 0, this.mKernel, 9, 9);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setColorAdjust(float adjust) {
/* 317 */     synchronized (this.mSync) {
/* 318 */       this.mColorAdjust = adjust;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTexSize(int width, int height) {
/* 326 */     this.mTexHeight = height;
/* 327 */     this.mTexWidth = width;
/* 328 */     float rw = 1.0F / width;
/* 329 */     float rh = 1.0F / height;
/*     */ 
/*     */     
/* 332 */     synchronized (this.mSync) {
/* 333 */       this.mTexOffset = new float[] { -rw, -rh, 0.0F, -rh, rw, -rh, -rw, 0.0F, 0.0F, 0.0F, rw, 0.0F, -rw, rh, 0.0F, rh, rw, rh };
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFlags(int[] flags) {
/* 342 */     int n = Math.min(4, (flags != null) ? flags.length : 0);
/* 343 */     if (n > 0) {
/* 344 */       synchronized (this.mSync) {
/* 345 */         System.arraycopy(flags, 0, this.mFlags, 0, n);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public void setFlag(int index, int value) {
/* 351 */     if (index >= 0 && index < this.mFlags.length) {
/* 352 */       synchronized (this.mSync) {
/* 353 */         this.mFlags[index] = value;
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
/*     */   public void draw(float[] mvpMatrix, int mvpMatrixOffset, FloatBuffer vertexBuffer, int firstVertex, int vertexCount, int coordsPerVertex, int vertexStride, float[] texMatrix, int texMatrixOffset, FloatBuffer texBuffer, int textureId, int texStride) {
/* 377 */     GLHelper.checkGlError("draw start");
/*     */ 
/*     */     
/* 380 */     GLES20.glUseProgram(this.mProgramHandle);
/* 381 */     GLHelper.checkGlError("glUseProgram");
/*     */ 
/*     */     
/* 384 */     GLES20.glActiveTexture(33984);
/* 385 */     GLES20.glBindTexture(this.mTextureTarget, textureId);
/* 386 */     GLHelper.checkGlError("glBindTexture");
/*     */     
/* 388 */     synchronized (this.mSync) {
/*     */       
/* 390 */       GLES20.glUniformMatrix4fv(this.muMVPMatrixLoc, 1, false, mvpMatrix, mvpMatrixOffset);
/* 391 */       GLHelper.checkGlError("glUniformMatrix4fv");
/*     */ 
/*     */       
/* 394 */       if (this.muTexMatrixLoc >= 0) {
/* 395 */         GLES20.glUniformMatrix4fv(this.muTexMatrixLoc, 1, false, texMatrix, texMatrixOffset);
/* 396 */         GLHelper.checkGlError("glUniformMatrix4fv");
/*     */       } 
/*     */ 
/*     */       
/* 400 */       GLES20.glEnableVertexAttribArray(this.maPositionLoc);
/* 401 */       GLHelper.checkGlError("glEnableVertexAttribArray");
/* 402 */       GLES20.glVertexAttribPointer(this.maPositionLoc, coordsPerVertex, 5126, false, vertexStride, vertexBuffer);
/*     */       
/* 404 */       GLHelper.checkGlError("glVertexAttribPointer");
/*     */ 
/*     */       
/* 407 */       GLES20.glEnableVertexAttribArray(this.maTextureCoordLoc);
/* 408 */       GLHelper.checkGlError("glEnableVertexAttribArray");
/* 409 */       GLES20.glVertexAttribPointer(this.maTextureCoordLoc, 2, 5126, false, texStride, texBuffer);
/*     */       
/* 411 */       GLHelper.checkGlError("glVertexAttribPointer");
/*     */ 
/*     */       
/* 414 */       if (this.muKernelLoc >= 0) {
/* 415 */         if (!this.mHasKernel2) {
/* 416 */           GLES20.glUniform1fv(this.muKernelLoc, 9, this.mKernel, 0);
/*     */         } else {
/* 418 */           GLES20.glUniform1fv(this.muKernelLoc, 18, this.mKernel, 0);
/*     */         } 
/* 420 */         GLHelper.checkGlError("set kernel");
/*     */       } 
/*     */       
/* 423 */       if (this.muTexOffsetLoc >= 0 && this.mTexOffset != null) {
/* 424 */         GLES20.glUniform2fv(this.muTexOffsetLoc, 9, this.mTexOffset, 0);
/*     */       }
/*     */       
/* 427 */       if (this.muColorAdjustLoc >= 0) {
/* 428 */         GLES20.glUniform1f(this.muColorAdjustLoc, this.mColorAdjust);
/*     */       }
/*     */       
/* 431 */       if (this.muTouchPositionLoc >= 0) {
/* 432 */         GLES20.glUniform2fv(this.muTouchPositionLoc, 1, this.mSummedTouchPosition, 0);
/*     */       }
/*     */       
/* 435 */       if (this.muFlagsLoc >= 0) {
/* 436 */         GLES20.glUniform1iv(this.muFlagsLoc, 4, this.mFlags, 0);
/*     */       }
/*     */     } 
/*     */     
/* 440 */     internal_draw(firstVertex, vertexCount);
/*     */ 
/*     */     
/* 443 */     GLES20.glDisableVertexAttribArray(this.maPositionLoc);
/* 444 */     GLES20.glDisableVertexAttribArray(this.maTextureCoordLoc);
/* 445 */     GLES20.glBindTexture(this.mTextureTarget, 0);
/* 446 */     GLES20.glUseProgram(0);
/*     */   }
/*     */   
/*     */   protected void initLocation(float[] kernel, float[] kernel2) {
/* 450 */     this.muKernelLoc = GLES20.glGetUniformLocation(this.mProgramHandle, "uKernel");
/* 451 */     if (this.muKernelLoc < 0) {
/*     */       
/* 453 */       this.muKernelLoc = -1;
/* 454 */       this.muTexOffsetLoc = -1;
/*     */     } else {
/*     */       
/* 457 */       this.muTexOffsetLoc = GLES20.glGetUniformLocation(this.mProgramHandle, "uTexOffset");
/* 458 */       if (this.muTexOffsetLoc < 0) {
/* 459 */         this.muTexOffsetLoc = -1;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 464 */       if (kernel == null) {
/* 465 */         kernel = ShaderConst.KERNEL_NULL;
/*     */       }
/* 467 */       setKernel(kernel, 0.0F);
/* 468 */       setTexSize(256, 256);
/*     */     } 
/* 470 */     if (kernel2 != null) {
/* 471 */       setKernel2(kernel2);
/*     */     }
/*     */     
/* 474 */     this.muColorAdjustLoc = GLES20.glGetUniformLocation(this.mProgramHandle, "uColorAdjust");
/* 475 */     if (this.muColorAdjustLoc < 0) {
/* 476 */       this.muColorAdjustLoc = -1;
/*     */     }
/*     */ 
/*     */     
/* 480 */     this.muTouchPositionLoc = GLES20.glGetUniformLocation(this.mProgramHandle, "uPosition");
/* 481 */     if (this.muTouchPositionLoc < 0)
/*     */     {
/* 483 */       this.muTouchPositionLoc = -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 488 */     this.muFlagsLoc = GLES20.glGetUniformLocation(this.mProgramHandle, "uFlags");
/* 489 */     if (this.muFlagsLoc < 0) {
/* 490 */       this.muFlagsLoc = -1;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void internal_draw(int firstVertex, int vertexCount) {
/* 497 */     GLES20.glDrawArrays(5, firstVertex, vertexCount);
/* 498 */     GLHelper.checkGlError("glDrawArrays");
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\Texture2dProgram.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */