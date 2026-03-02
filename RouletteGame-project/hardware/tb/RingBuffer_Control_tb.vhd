library ieee;
use ieee.std_logic_1164.all;

entity RingBuffer_Control_tb is
end RingBuffer_Control_tb;

architecture behavioral of RingBuffer_Control_tb is

component RingBuffer_Control is
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
end component;

constant MCLK_PERIOD : time := 20 ns;
constant MCLK_HALF_PERIOD : time := MCLK_PERIOD / 2;

signal DAV_tb : std_logic := '0';
signal RESET_tb : std_logic := '0';
signal CLK_tb : std_logic := '0';
signal CTS_tb : std_logic := '0';
signal full_tb : std_logic := '0';
signal empty_tb : std_logic := '0';
signal Wr_tb : std_logic;
signal selPG_tb : std_logic;
signal Wreg_tb : std_logic;
signal DAC_tb : std_logic;
signal incPut_tb : std_logic;
signal incGet_tb : std_logic;

begin

UTT : RingBuffer_Control 
          port map(
	      DAV => DAV_tb,
	      RESET => Reset_tb,
	      CLK => CLK_tb,
	      CTS => CTS_tb,
	      full => full_tb,
	      empty => empty_tb,
	      Wr => Wr_tb,
	      selPG => selPG_tb,
	      Wreg => Wreg_tb,
	      DAC => DAC_tb,
	      incPut => incPut_tb,
	      incGet => incGet_tb);

clk_gen : process
begin 
	CLK_tb <= '0';
	wait for MCLK_HALF_PERIOD;
	CLK_tb <= '1';
	wait for MCLK_HALF_PERIOD;
end process;


stimulus : process
begin

RESET_tb <= '1';
wait for MCLK_PERIOD;
RESET_tb <= '0';
wait for MCLK_PERIOD;

-- nada
DAV_tb <= '1';
full_tb <= '1';
wait for MCLK_PERIOD;

-- ADDRESS_STATE
full_tb <= '0';
wait for MCLK_PERIOD * 6;

-- DATAACCEPTED_STATE
DAV_tb <= '1';
wait for MCLK_PERIOD;

-- INITIAL_STATE
DAV_tb <= '0';
wait for MCLK_PERIOD;

--INITIAL_STATE
CTS_tb <= '0';
wait for MCLK_PERIOD;

-- INITIAL_STATE
CTS_tb <= '1';
empty_tb <= '1';
wait for MCLK_PERIOD;

-- READ_STATE
empty_tb <= '0';
wait for MCLK_PERIOD * 4;

-- READ_STATE
CTS_tb <= '1';
wait for MCLK_PERIOD;

-- RNEXT_STATE
CTS_tb <= '0';
wait for MCLK_PERIOD * 4;


wait;
end process;

end behavioral;