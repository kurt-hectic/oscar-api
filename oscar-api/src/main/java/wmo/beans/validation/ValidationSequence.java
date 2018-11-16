package wmo.beans.validation;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;
 
@GroupSequence({Default.class, SecondaryValidation.class})
public interface ValidationSequence {
}