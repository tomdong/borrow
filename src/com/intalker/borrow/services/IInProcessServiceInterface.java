package com.intalker.borrow.services;

public interface IInProcessServiceInterface {
	
	public interface IExecuator
	{
		public void process();
	}
	
	
	public interface ITimerTimeout
	{
		public void onTimeOut();
	}
	
	// client will request service to do something according to the contract!
	public void request(IExecuator c);
	
	// client request service to execute in seperate thread for ITimerTimeout!
	public void requestTimeout(ITimerTimeout t);
	public void removeTimeout(ITimerTimeout t);

}
