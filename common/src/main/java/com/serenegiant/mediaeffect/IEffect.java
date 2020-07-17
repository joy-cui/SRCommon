package com.serenegiant.mediaeffect;

public interface IEffect {
  void apply(int[] paramArrayOfint, int paramInt1, int paramInt2, int paramInt3);
  
  void apply(ISource paramISource);
  
  void release();
  
  IEffect resize(int paramInt1, int paramInt2);
  
  boolean enabled();
  
  IEffect setEnable(boolean paramBoolean);
}


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\IEffect.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */