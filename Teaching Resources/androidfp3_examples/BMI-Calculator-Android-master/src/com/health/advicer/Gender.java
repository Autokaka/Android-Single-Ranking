package com.health.advicer;

public enum Gender {

	Male(66, 13.7, 5.0, 6.8), Female(655, 9.6, 1.8, 4.7);

	private Integer sumFactor;
	private Double weightFactor;
	private Double heightFactor;
	private Double ageFactor;

	private Gender(Integer sumFactor, Double weightFactor, Double heightFactor,
			Double ageFactor) {
		this.sumFactor = sumFactor;
		this.weightFactor = weightFactor;
		this.heightFactor = heightFactor;
		this.ageFactor = ageFactor;
	}

	public Double calculateBMR(Double weight, Double height, Integer age) {
		return sumFactor + (weightFactor * weight) + (heightFactor * height)
				- (ageFactor * age);
	}

}
