package test2;

import etu2020.framework.annotation.*;

@ModelAnnotation
public class Test2{
	@MethodAnnotation(url="sayhello")
	public void sayHello(){}
}
