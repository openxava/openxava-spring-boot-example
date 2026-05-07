package com.yourcompany.invoicing.calculators;

import static org.openxava.jpa.XPersistence.getManager;

import org.openxava.calculators.*;

import com.yourcompany.invoicing.model.*;

import lombok.*;

public class UnitPriceCalculator implements ICalculator {
	
	@Getter @Setter
	int number;  

	public Object calculate() throws Exception {
		return getManager().find(Product.class, number).getUnitPrice();  
	}

}
