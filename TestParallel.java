package suite;

import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;
// Parallel among classes
//JUnitCore.runClasses(ParallelComputer.classes(), cls);
// Parallel among methods in a class
//JUnitCore.runClasses(ParallelComputer.methods(), cls);
// Parallel all methods in all classes - ParallelComputer(classes, methods)
//JUnitCore.runClasses(new ParallelComputer(true, true), cls);
public class TestParallel {
    @Test
    public void testAtLocal() {
		Class[] cls = {
				T00S01_WebLogin_2S4F.class,
				T02S01A_ix_request_laboratory.class,
				T01S05_discharge_prescription_OPMOE.class,
		};
	    JUnitCore.runClasses(ParallelComputer.classes(), cls);
    }
    @Test
    public void testAtRemoteB() {
		Class[] cls = {
				T00S01_WebLogin_2S4Fremote.class,
				T01S03_corp_allergy.class,
				T01S06_Discharge_Summary.class,
				T03S01_PMI_enquiry.class,
				T05S01_bed_assignment.class,
				T05S02_transfer.class,
				T05S03_swap_bed.class,
				T05S04_discharge.class,
				T05S05_modification_of_transfer.class,
				T05S06_modification_of_discharge.class,
				T05S07_trial_discharge.class,
				T05S08_return_from_trial_discharge.class,
				T05S09_cancellation_of_previous_transactions.class,
				T07S03_medical_certificate.class,
				T01S12_IPMOE_Prescription.class,
		};
		JUnitCore.runClasses(ParallelComputer.classes(), cls);
    }
}