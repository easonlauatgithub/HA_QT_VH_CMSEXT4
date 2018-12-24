/*	T00S01_WebLogin_2S4F.class,
	T01S03_corp_allergy.class,
	T01S06_Discharge_Summary.class,
	T03S01_PMI_enquiry.class,
	T02S01A_ix_request_laboratory.class,
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
	T01S05_discharge_prescription_OPMOE.class,
	T01S12_IPMOE_Prescription.class,*/
package suite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	T00S01_WebLogin_2S4F.class,
	//T00S01_WebLogin_2S4Fremote.class,
	//T01S05_discharge_prescription_OPMOE.class,
	T01S12_IPMOE_Prescription.class,

	

/*
HUB
cd C:\eclipse_neon
"java\jdk1.8.0_181\bin\java" -jar "workspace\lib\selenium\3.14\selenium-server-standalone-3.14.0.jar" -role hub -port 4444
NODE 1
cd C:\easontesting\3.14
java -Dwebdriver.ie.driver="IEDriver32Server.exe" -jar selenium-server-standalone-3.14.0.jar -role node -port 5555 -hub http://160.77.18.138:4444/grid/register
NODE 2
cd C:\eclipse_neon
"java\jdk1.8.0_181\bin\java" -jar "workspace\lib\selenium\3.14\selenium-server-standalone-3.14.0.jar" -role node -port 5556 -hub http://160.77.18.138:4444/grid/register

	T00S01_WebLogin_2S4Fremote.class,
	T01S03_corp_allergy.class,
	T01S06_Discharge_Summary.class,
	T03S01_PMI_enquiry.class,
	//T02S01A_ix_request_laboratory.class,
	T00S01_WebLogin_2S4Fremote.class,
	T05S01_bed_assignment.class,
	T05S02_transfer.class,
	T05S03_swap_bed.class,
	T05S04_discharge.class,
	T05S05_modification_of_transfer.class,
	T05S06_modification_of_discharge.class,
	T05S07_trial_discharge.class,
	T05S08_return_from_trial_discharge.class,
	T05S09_cancellation_of_previous_transactions.class,
	T00S01_WebLogin_2S4Fremote.class,
	T07S03_medical_certificate.class,
	//T01S05_discharge_prescription_OPMOE.class,
	T01S12_IPMOE_Prescription.class,
	T00S01_WebLogin_2S4F.class,
	T01S05_discharge_prescription_OPMOE.class,
	T02S01A_ix_request_laboratory.class,
	
*/
})
public class TestSuite {}
