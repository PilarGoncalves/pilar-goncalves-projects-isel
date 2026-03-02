library ieee;
use ieee.std_logic_1164.all;

entity RingBuffer is
	port(
	D : in std_logic_vector(3 downto 0);
	DAV : in std_logic;
	CTS : in std_logic;
	RESET : in std_logic;
	CLK : in std_logic;
	Wreg : out std_logic;
	DAC : out std_logic;
	Q : out std_logic_vector(3 downto 0)
	);
end RingBuffer;
	
architecture structural of RingBuffer is

component MAC is 
	port(
	putGet: in std_logic;
	incPut: in std_logic;
	incGet: in std_logic;
	CLK: in std_logic;
	RESET: in std_logic;
	full: out std_logic;
	empty: out std_logic;
	A: out std_logic_vector(3 downto 0)
	);
end component;

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

component RAM is
	generic(
		ADDRESS_WIDTH : natural := 4;
		DATA_WIDTH : natural := 4
	);
	port(
		address : in std_logic_vector(ADDRESS_WIDTH - 1 downto 0);
		wr: in std_logic;
		din: in std_logic_vector(DATA_WIDTH - 1 downto 0);
		dout: out std_logic_vector(DATA_WIDTH - 1 downto 0)
	);
end component;

signal wr_signal: std_logic;
signal selPG_putget: std_logic;
signal MAC_A: std_logic_vector(3 downto 0);
signal incGet_signal: std_logic;
signal incPut_signal: std_logic;
signal full_signal: std_logic;
signal empty_signal: std_logic;

begin

U0 : MAC port map (
	putGet => selPG_putget,
	incPut => incPut_signal,
	incGet => incGet_signal,
	CLK => CLK,
	RESET => RESET,
	full => full_signal,
	empty => empty_signal,
	A => MAC_A
	);

U1 : RingBuffer_Control port map (
	DAV => DAV,
	CLK => CLK,
	RESET => RESET,
	CTS => CTS,
	full => full_signal,
	empty => empty_signal,
	Wr => wr_signal,
	selPG => selPG_putget,
	Wreg => Wreg,
	DAC => DAC,
	incPut => incPut_signal,
	incGet => incGet_signal
	);

U2 : RAM port map (
	address => MAC_A,
	wr => wr_signal,
	din => D,
	dout => Q
	);

end structural;