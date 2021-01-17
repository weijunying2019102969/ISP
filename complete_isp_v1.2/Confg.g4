grammar Confg;

prog : stat+; 

stat : expr  # printExpr
      ;

expr :
	 IPV4			#print
	 |IPV6			#print
	 |NEWLINE		#print
     ;

IPV4 : 'ip route' .*? '\r'? '\n';
IPV6 : 'ipv6 route' .*? '\r'? '\n';

NEWLINE : '\r'? '\n' ; 
COMMENT : ~ ('\n' | '\r') *  'ip route' {skip();} ;
A_COMMENT : 'a' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
B_COMMENT : 'b' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
C_COMMENT : 'c' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
D_COMMENT : 'd' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
E_COMMENT : 'e' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
F_COMMENT : 'f' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
G_COMMENT : 'g' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
H_COMMENT : 'h' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
J_COMMENT : 'j' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
K_COMMENT : 'k' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
L_COMMENT : 'l' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
M_COMMENT : 'm' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
N_COMMENT : 'n' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
O_COMMENT : 'o' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
P_COMMENT : 'p' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
Q_COMMENT : 'q' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
R_COMMENT : 'r' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
S_COMMENT : 's' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
T_COMMENT : 't' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
U_COMMENT : 'u' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
V_COMMENT : 'v' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
W_COMMENT : 'w' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
X_COMMENT : 'x' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
Y_COMMENT : 'y' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
Z_COMMENT : 'z' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
IPA_COMMENT : 'ip a' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
IPD_COMMENT : 'ip domain' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
IPV_COMMENT : 'ipv6' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
IPS_COMMENT : 'ip ssh' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
IPP_COMMENT : 'ip policy' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
IPF_COMMENT : 'ip f' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
IPL_COMMENT : 'ip local' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
IPN_COMMENT : 'ip nat' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
INT_COMMENT : 'interface' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;
CHAR_COMMENT : '!' ~ ('\n' | '\r') *  '\r'? '\n' {skip();} ;

WS :[ \t] -> skip ;