library ieee;
use ieee.std_logic_1164.all;

entity MAC is 
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
end MAC;

architecture structural of MAC is

component CounterUD_MAC is 
	port (
	CE,CLK,RESET, UP_DOWN: in std_logic; 
	Q: out std_logic_vector (4 downto 0) 
	);
end component;

component Counter_MAC is 
	port (
	CE,CLK,RESET : in std_logic ; 
	Q: out std_logic_vector (3 downto 0) 
	);
end component;

component MUX2_1 is
	port (	
	A, B : in std_logic_vector(3 downto 0);
	S: in std_logic;
   Y : out std_logic_vector (3 downto 0)
   ); 
end component;

component comparator_MAC is 
	port (
	A: in std_logic_vector (4 downto 0); 
	B: in std_logic_vector (4 downto 0); 
	TC: out std_logic
	); 
end component;

signal getCounter_mux : std_logic_vector(3 downto 0);
signal putCounter_mux : std_logic_vector(3 downto 0);
signal CounterUD_cmp : std_logic_vector(4 downto 0);
signal or_signal : std_logic;

begin 
U0: Counter_MAC port map ( CE => incGet, CLK => CLK, RESET => RESET, Q => getCounter_mux );

U1: Counter_MAC port map ( CE => incPut, CLK => CLK, RESET => RESET, Q => putCounter_mux );

U2: CounterUD_MAC port map ( CE => or_signal, CLK => CLK, RESET => RESET, UP_DOWN => incGet, Q => CounterUD_cmp );

U3: MUX2_1 port map ( A => getCounter_mux, B => putCounter_mux, S => putGet, Y => A );

U4: comparator_MAC port map ( A => CounterUD_cmp, B => "00000", TC => empty );

U5: comparator_MAC port map ( A => CounterUD_cmp, B => "10000", TC => full );

or_signal <= incPut or incGet;

end structural;









