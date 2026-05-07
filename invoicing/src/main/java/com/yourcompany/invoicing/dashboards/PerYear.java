package com.yourcompany.invoicing.dashboards;

import java.math.*;

import lombok.*;

@Getter @Setter @AllArgsConstructor 
public class PerYear {
	
	int year;
	
	BigDecimal total;
	
	BigDecimal tax;

}
