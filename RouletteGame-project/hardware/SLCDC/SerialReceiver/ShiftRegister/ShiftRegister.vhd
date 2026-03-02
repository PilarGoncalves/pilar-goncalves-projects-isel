library ieee;
use ieee.std_logic_1164.all;

entity ShiftRegister is 
	port(
	data: in std_logic;
	enableShift: in std_logic;
	CLK: in std_logic;
	D: out std_logic_vector(4 downto 0)
	);
end ShiftRegister;

architecture structural of ShiftRegister is

component ffdSR is
	port(
	CLK : in std_logic;
	RESET : in std_logic;
	SET : in std_logic;
	D : in std_logic;
	EN: in std_logic;
	Q : out std_logic
	);
end component;

signal exitFFD: std_logic_vector(4 downto 0);

begin 

U1: ffdSR port map (CLK => CLK, RESET => '0', SET => '0', D => data, EN => enableShift, Q => exitFFD(0));
U2: ffdSR port map (CLK => CLK, RESET => '0', SET => '0', D => exitFFD(0), EN => enableShift, Q => exitFFD(1));
U3: ffdSR port map (CLK => CLK, RESET => '0', SET => '0', D => exitFFD(1), EN => enableShift, Q => exitFFD(2));
U4: ffdSR port map (CLK => CLK, RESET => '0', SET => '0', D => exitFFD(2), EN => enableShift, Q => exitFFD(3)); 
U5: ffdSR port map (CLK => CLK, RESET => '0', SET => '0', D => exitFFD(3), EN => enableShift, Q => exitFFD(4));

D(0) <= exitFFD(4);
D(1) <= exitFFD(3);
D(2) <= exitFFD(2);
D(3) <= exitFFD(1);
D(4) <= exitFFD(0); 
 
end structural;