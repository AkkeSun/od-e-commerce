package com.product.global.validation.groups;

import com.product.global.validation.groups.ValidationGroups.CustomGroups;
import com.product.global.validation.groups.ValidationGroups.NotBlankGroups;
import com.product.global.validation.groups.ValidationGroups.SizeGroups;
import jakarta.validation.GroupSequence;

@GroupSequence({NotBlankGroups.class, SizeGroups.class, CustomGroups.class})
public interface ValidationSequence {

}
