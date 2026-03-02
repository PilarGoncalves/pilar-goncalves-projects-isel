library ieee;
use ieee.std_logic_1164.all;

entity SerialControl is 
	port( 
	RESET : in std_logic;
	CLK : in std_logic;
	enRx : in std_logic;
	accept : in std_logic;
   pFlag : in std_logic;
   dFlag : in std_logic;
	RXerror : in std_logic;
   wr : out std_logic;
	init : out std_logic;
	DXval : out std_logic
	);
end SerialControl;

architecture behavioral of SerialControl is

type STATE_TYPE is (INITIAL_STATE, RECEIVING_STATE, PARITYCHECK_STATE, SENT_STATE, WAITACCEPT_STATE);

signal CurrentState, NextState : STATE_TYPE;

begin

CurrentState <= INITIAL_STATE when RESET = '1' else NextState when rising_edge(CLK);

GenerateNextState:
process (CurrentState, enRX , accept , pFlag , dFlag , RXerror)
	begin
		case CurrentState is
			when INITIAL_STATE	=> if (enRX = '1') then 
												NextState <= RECEIVING_STATE;
											else 
												NextState <= INITIAL_STATE;
											end if;
											
			when RECEIVING_STATE		=> if (enRX = '1' and dFlag = '1') then 
													NextState <= PARITYCHECK_STATE;
												elsif (enRX = '0') then
													NextState <= INITIAL_STATE;
												else 
													NextState <= RECEIVING_STATE;
												end if;
											
			when PARITYCHECK_STATE		=> if (enRX = '0' and pFlag = '1'and RXerror = '0') then 
														NextState <= SENT_STATE;
													elsif (enRX = '1') then
														NextState <= PARITYCHECK_STATE;
													else
														NextState <= INITIAL_STATE;
													end if;
											
			when SENT_STATE		=> if (accept = '1') then 
												NextState <= WAITACCEPT_STATE;
											else
												NextState <= SENT_STATE;
											end if;
			
			when WAITACCEPT_STATE		=> if (accept = '0') then 
												NextState <= INITIAL_STATE;
											else
												NextState <= WAITACCEPT_STATE;
											end if;
		end case;
	end process;		

init <= '1' when (CurrentState = INITIAL_STATE) else '0';
wr <= '1' when (CurrentState = RECEIVING_STATE) else '0';
DXval <= '1' when (CurrentState = SENT_STATE) else '0';

end behavioral;	
 
 
 
 
 
 
 
 
 
 
 
 
 
 