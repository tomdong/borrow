package com.intalker.borrow.services;

public interface IInProcessServiceInterface {
	public interface IInProcessClientInterface
	{
		// Service will notify client through this when something is done.
		// It will grantee that the contract is the same as reqested!
		// Carefull!!!! this message is not execute in Client's thread, you need to translate it if you care about it!
		public void onResult(IContract c);
		
		// when something that service monitored happen, client will be notify by this message!
		public void onPushMessage(IInProcessPushMessage m);
	}
	
	public interface IContract
	{
		public IRequest getRequest();
		public IResult getResult();
		
		// process the contract, please try to grantee the process can be mutli-threaded, although currently,
		// service will make sure the contract is process one by one!
		public void process();
	}
	
	public interface IResult
	{
		
	}
	public interface IRequest
	{
		
	}
	
	public interface IInProcessPushMessage
	{
		
	}
	
	// set the client that service will inform when something is done.
	public void setClient(IInProcessClientInterface i);
	
	// client will request service to do something according to the contract!
	public void request(IContract c);

}
