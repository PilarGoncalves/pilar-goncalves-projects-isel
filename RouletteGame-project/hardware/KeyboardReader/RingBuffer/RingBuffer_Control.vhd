library ieee;
use ieee.std_logic_1164.all;

entity RingBuffer_Control is
	port(
	DAV : in std_logic;
	CLK : in std_logic;
	RESET : in std_logic;
	CTS : in std_logic;
	full : in std_logic;
	empty : in std_logic;
	Wr : out std_logic;
	selPG : out std_logic;
	Wreg : out std_logic;
	DAC : out std_logic;
	incPut : out std_logic;
	incGet : out std_logic
	);
end RingBuffer_Control;

architecture behavioral of RingBuffer_Control is

type STATE_TYPE is (INITIAL_STATE, ADDRESS_STATE, WRITE_STATE, WNEXT_STATE, DATAACCEPTED_STATE, READ_STATE, RNEXT_STATE);

signal Current_State, Next_State : STATE_TYPE;

begin

Current_State <= INITIAL_STATE when RESET = '1' else Next_State when rising_edge(clk);

GenerateNextState:
process (Current_State, DAV, CTS, full, empty)
	begin
		case Current_State is
			when INITIAL_STATE => if (DAV = '1' and full = '0') then
											Next_State <= ADDRESS_STATE;
										 elsif (DAV = '1' and full = '1' and CTS = '1' and empty = '0') then
											Next_State <= READ_STATE;
										 elsif (DAV = '0' and CTS = '1' and empty = '0') then 
											Next_State <= READ_STATE;
										 else 
											Next_State <= INITIAL_STATE;
										 end if;
									
		when ADDRESS_STATE => Next_State <= WRITE_STATE;
		
		when WRITE_STATE => Next_State <= WNEXT_STATE;
		
		when WNEXT_STATE => Next_State <= DATAACCEPTED_STATE;
		
		when DATAACCEPTED_STATE => if (DAV = '1') then
												Next_State <= DATAACCEPTED_STATE;
											else 
												Next_State <= INITIAL_STATE;
											end if;
		
		when READ_STATE => if (CTS = '1') then
									Next_State <= READ_STATE;
								 else
									Next_State <= RNEXT_STATE;
								 end if;
		
		when RNEXT_STATE => Next_State <= INITIAL_STATE;
		
								

		end case;
end process;

selPG <= '1' when (Current_State = ADDRESS_STATE or Current_State = WRITE_STATE or Current_State = WNEXT_STATE) else '0';

Wr <= '1' when (Current_State = WRITE_STATE) else '0';

incPut <= '1' when (Current_State = WNEXT_STATE) else '0';

DAC <= '1' when (Current_State = DATAACCEPTED_STATE) else '0';

Wreg <= '1' when (Current_State = READ_STATE) else '0';

incGet <= '1' when (Current_State = RNEXT_STATE) else '0';

end behavioral;